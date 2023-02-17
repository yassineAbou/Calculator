package com.example.calculator.ui.calculator

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.R
import com.example.calculator.databinding.FragmentCalculatorBinding
import com.example.calculator.ui.emi.EMIViewModel
import com.example.calculator.ui.emi.EmiCalculatorState
import com.example.calculator.util.isDarkMode
import com.example.calculator.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalculatorFragment : Fragment(R.layout.fragment_calculator) {

    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private val emiViewModel: EMIViewModel by activityViewModels()
    private val fragmentCalculatorBinding by viewBinding(FragmentCalculatorBinding::bind)
    private val adapter by lazy {
        ListPreviousOperationsAdapter(object : PreviousOperationAction {
            override fun addPreviousNumber(number: String) {
                calculatorViewModel.addToCurrentInput(number)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentCalculatorBinding.viewModel = calculatorViewModel
        fragmentCalculatorBinding.lifecycleOwner = viewLifecycleOwner

        fragmentCalculatorBinding.listPrevOperations.adapter = adapter
        val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        fragmentCalculatorBinding.listPrevOperations.layoutManager = manager

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                fragmentCalculatorBinding.listPrevOperations.adapter = null
                super.onDestroy(owner)
            }
        })

        fragmentCalculatorBinding.apply {
            emiCalculator.setOnClickListener {
                navigateToEmiCalculator()
            }

            normalMode?.setOnClickListener {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            scientificMode?.setOnClickListener {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            alternativeFunctions?.setOnClickListener {
                changeGroupFunctionsVisibility()
            }

            history.setOnClickListener {
                changeHistoryVisibility()
                fragmentCalculatorBinding.listPrevOperations.scrollToPosition(0)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calculatorViewModel.currentInput.collectLatest {
                    it?.let {
                        calculatorViewModel.changeCurrentInput(it)
                        val backSpaceColor = if (it.isNotEmpty()) "#997592" else "#D8BFD8"
                        fragmentCalculatorBinding.backspace.setColorFilter(
                            Color.parseColor(backSpaceColor),
                            PorterDuff.Mode.SRC_ATOP
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calculatorViewModel.result.collectLatest {
                    it?.let {
                        calculatorViewModel.changeResult(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calculatorViewModel.calculatorState.collect { calculatorState ->
                    when {
                        calculatorState.isInvalidFormat -> showInvalidFormat()
                        calculatorState.isSecondGroupFunctions -> showSecondGroupFunctions()
                        calculatorState.isFirstGroupFunctions -> showFirstGroupFunctions()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calculatorViewModel.listPreviousOperationsFlow.collect {
                    adapter.submitList(it)

                    when (it.size) {
                        0 -> {
                            disableHistoryButton()
                            hideHistoryGroup()
                        }
                        else -> enableHistoryButton()
                    }
                }
            }
        }
    }

    private fun changeGroupFunctionsVisibility() {
        if (calculatorViewModel.calculatorState.value.isSecondGroupFunctions) {
            calculatorViewModel.changeCalculatorState(
                CalculatorState(isFirstGroupFunctions = true, isSecondGroupFunctions = false)
            )
        } else {
            calculatorViewModel.changeCalculatorState(
                CalculatorState(isFirstGroupFunctions = false, isSecondGroupFunctions = true)
            )
        }
    }

    private fun enableHistoryButton() {
        fragmentCalculatorBinding.history.isEnabled = true
        fragmentCalculatorBinding.history.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black_200),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun disableHistoryButton() {
        fragmentCalculatorBinding.history.isEnabled = false
        val disabledColor = if (requireContext().isDarkMode()) "#333333" else "#CACACA"
        fragmentCalculatorBinding.history.setColorFilter(
            Color.parseColor(disabledColor),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun showInvalidFormat() {
        Toast.makeText(requireContext(), "Invalid format used.", Toast.LENGTH_SHORT).show()
        calculatorViewModel.changeCalculatorState(CalculatorState(isInvalidFormat = false))
    }

    private fun showFirstGroupFunctions() {
        fragmentCalculatorBinding.secondGroupFunctions?.visibility = GONE
        fragmentCalculatorBinding.firstGroupFunctions?.visibility = VISIBLE
    }

    private fun showSecondGroupFunctions() {
        fragmentCalculatorBinding.firstGroupFunctions?.visibility = INVISIBLE
        fragmentCalculatorBinding.secondGroupFunctions?.visibility = VISIBLE
    }

    private fun changeHistoryVisibility() {
        fragmentCalculatorBinding.apply {
            if (historyGroup.isVisible) {
                hideHistoryGroup()
            } else {
                showHistoryGroup()
            }
        }
    }

    private fun hideHistoryGroup() {
        fragmentCalculatorBinding.apply {
            history.setImageResource(R.drawable.history)
            historyGroup.visibility = GONE
            normalModeGroup.visibility = VISIBLE

            if (calculatorViewModel.calculatorState.value.isSecondGroupFunctions) {
                secondGroupFunctions?.visibility = VISIBLE
            } else {
                firstGroupFunctions?.visibility = VISIBLE
            }
        }
    }

    private fun showHistoryGroup() {
        fragmentCalculatorBinding.apply {
            history.setImageResource(R.drawable.ic_baseline_calculate_24)
            historyGroup.visibility = VISIBLE
            normalModeGroup.visibility = INVISIBLE

            if (calculatorViewModel.calculatorState.value.isSecondGroupFunctions) {
                secondGroupFunctions?.visibility = INVISIBLE
            } else {
                firstGroupFunctions?.visibility = INVISIBLE
            }
        }
    }

    private fun navigateToEmiCalculator() {
        emiViewModel.changeEmiCalculatorState(
            EmiCalculatorState(isFirstEmiCalculator = true, isSecondEmiCalculator = false)
        )
        val action = CalculatorFragmentDirections.actionCalculatorFragmentToEmiCalculator()
        findNavController(this).navigate(action)
    }
}

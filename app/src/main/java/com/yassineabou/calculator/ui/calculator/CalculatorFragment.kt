package com.yassineabou.calculator.ui.calculator

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
import com.yassineabou.calculator.R
import com.yassineabou.calculator.databinding.FragmentCalculatorBinding
import com.yassineabou.calculator.ui.emi.EMIViewModel
import com.yassineabou.calculator.ui.emi.EmiCalculatorState
import com.yassineabou.calculator.util.isDarkMode
import com.yassineabou.calculator.util.viewBinding
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
            override fun appendNumberFromHistory(number: String) {
                calculatorViewModel.appendNumberFromHistory(number)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentCalculatorBinding.viewModel = calculatorViewModel
        fragmentCalculatorBinding.lifecycleOwner = viewLifecycleOwner

        fragmentCalculatorBinding.listPreviousOperations.adapter = adapter
        val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        fragmentCalculatorBinding.listPreviousOperations.layoutManager = manager

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                fragmentCalculatorBinding.listPreviousOperations.adapter = null
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
                fragmentCalculatorBinding.listPreviousOperations.scrollToPosition(0)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calculatorViewModel.input.collectLatest {
                    it?.let {
                        calculatorViewModel.updateInput(it)
                        val backSpaceColor = if (it.isNotEmpty()) "#997592" else "#D8BFD8"
                        fragmentCalculatorBinding.backspace.setColorFilter(
                            Color.parseColor(backSpaceColor),
                            PorterDuff.Mode.SRC_ATOP,
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calculatorViewModel.result.collectLatest {
                    it?.let {
                        calculatorViewModel.updateResult(it)
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
            calculatorViewModel.updateCalculatorState(
                CalculatorState(isFirstGroupFunctions = true, isSecondGroupFunctions = false),
            )
        } else {
            calculatorViewModel.updateCalculatorState(
                CalculatorState(isFirstGroupFunctions = false, isSecondGroupFunctions = true),
            )
        }
    }

    private fun enableHistoryButton() {
        fragmentCalculatorBinding.history.isEnabled = true
        fragmentCalculatorBinding.history.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black_200),
            PorterDuff.Mode.SRC_ATOP,
        )
    }

    private fun disableHistoryButton() {
        fragmentCalculatorBinding.history.isEnabled = false
        val disabledColor = if (requireContext().isDarkMode()) "#333333" else "#CACACA"
        fragmentCalculatorBinding.history.setColorFilter(
            Color.parseColor(disabledColor),
            PorterDuff.Mode.SRC_ATOP,
        )
    }

    private fun showInvalidFormat() {
        Toast.makeText(requireContext(), "Invalid format used.", Toast.LENGTH_SHORT).show()
        calculatorViewModel.updateCalculatorState(CalculatorState(isInvalidFormat = false))
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
        emiViewModel.updateEmiCalculatorState(
            EmiCalculatorState(isFirstEmiCalculator = true, isSecondEmiCalculator = false),
        )
        val action = CalculatorFragmentDirections.calculatorFragmentToEmiCalculator()
        findNavController(this).navigate(action)
    }
}


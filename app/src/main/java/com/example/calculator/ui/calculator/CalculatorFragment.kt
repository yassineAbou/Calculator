package com.example.calculator.ui.calculator

import android.content.pm.ActivityInfo
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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
import com.example.calculator.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalculatorFragment : Fragment(R.layout.fragment_calculator) {

    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private val emiViewModel : EMIViewModel by activityViewModels()
    private val fragmentCalculatorBinding by viewBinding(FragmentCalculatorBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCalculatorBinding.calculatorViewModel = calculatorViewModel
        fragmentCalculatorBinding.lifecycleOwner = viewLifecycleOwner


        val adapter = PrevOperationAdapter(PrevOperationActions { number ->
            calculatorViewModel.currentInput.value?.let { calculatorViewModel.addToCurrentInput(number) }
        })

        fragmentCalculatorBinding.listPrevOperations.adapter = adapter
        val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        fragmentCalculatorBinding.listPrevOperations.layoutManager = manager

        fragmentCalculatorBinding.apply {

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
                calculatorViewModel?.let {
                    val isFirstGroupFunctions = !it.calculatorState.value.isFirstGroupFunctions
                    val isSecondGroupFunctions = !it.calculatorState.value.isSecondGroupFunctions
                    if (isFirstGroupFunctions && isSecondGroupFunctions) {
                        it.onChangeCalculatorState(CalculatorState(isFirstGroupFunctions = true))
                    }
                    changeHistoryVisibility()
                    fragmentCalculatorBinding.listPrevOperations.scrollToPosition(0)
                }
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            calculatorViewModel.currentInput.collectLatest {
                it?.let {
                    if (it.isNotEmpty()) {
                        fragmentCalculatorBinding.backspace.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.purple),
                            PorterDuff.Mode.SRC_ATOP)
                    } else {
                        fragmentCalculatorBinding.backspace.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.purple_light),
                            PorterDuff.Mode.SRC_ATOP)
                    }
                    calculatorViewModel.onChangeCurrentInput(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            calculatorViewModel.result.collectLatest {
                it?.let {
                    calculatorViewModel.onChangeResult(it)
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
            calculatorViewModel.listPrevOperations.collectLatest { listPrevOperations ->
                adapter.submitList(listPrevOperations)

                when (listPrevOperations.size) {
                    0 -> {
                        disableHistoryButton()
                        hideHistoryGroup()
                    }
                    else -> enableHistoryButton()
                }

            }
        }



        fragmentCalculatorBinding.emiCalculator.setOnClickListener {
            navigateToEmiCalculator()
        }
    }

    private fun changeGroupFunctionsVisibility() {
        if (fragmentCalculatorBinding.firstGroupFunctions?.isVisible == true) {
            changeSecondFunctionsState()
        } else if (fragmentCalculatorBinding.secondGroupFunctions?.isVisible == true) {
            changeFirstFunctionsState()
        }
    }

    private fun changeFirstFunctionsState() {
        calculatorViewModel.onChangeCalculatorState(CalculatorState(isSecondGroupFunctions = false))
        calculatorViewModel.onChangeCalculatorState(CalculatorState(isFirstGroupFunctions = true))
    }

    private fun changeSecondFunctionsState() {
        calculatorViewModel.onChangeCalculatorState(CalculatorState(isFirstGroupFunctions = false))
        calculatorViewModel.onChangeCalculatorState(CalculatorState(isSecondGroupFunctions = true))
    }

    private fun enableHistoryButton() {
        fragmentCalculatorBinding.history.isEnabled = true
        fragmentCalculatorBinding.history.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.light_black),
            PorterDuff.Mode.SRC_ATOP)
    }

    private fun disableHistoryButton() {
        fragmentCalculatorBinding.history.isEnabled = false
        fragmentCalculatorBinding.history.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.gray_light),
            PorterDuff.Mode.SRC_ATOP)
    }


    private fun showInvalidFormat() {
        Toast.makeText(requireContext(), "Invalid format used.", Toast.LENGTH_SHORT).show()
        calculatorViewModel.onChangeCalculatorState(CalculatorState(isInvalidFormat = false))
    }

    private fun showFirstGroupFunctions() {
        fragmentCalculatorBinding.secondGroupFunctions?.visibility = GONE
        fragmentCalculatorBinding.firstGroupFunctions?.visibility = VISIBLE
    }

    private fun showSecondGroupFunctions() {
        fragmentCalculatorBinding.firstGroupFunctions?.visibility  = INVISIBLE
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
            normalModeGroup.visibility =  VISIBLE

            calculatorViewModel?.let {
                if (it.calculatorState.value.isFirstGroupFunctions) {
                    firstGroupFunctions?.visibility = VISIBLE
                } else if (it.calculatorState.value.isSecondGroupFunctions) {
                    secondGroupFunctions?.visibility = VISIBLE
                }
            }
        }
    }

    private fun showHistoryGroup() {
        fragmentCalculatorBinding.apply {
            history.setImageResource(R.drawable.ic_baseline_calculate_24)
            historyGroup.visibility = VISIBLE
            normalModeGroup.visibility =  INVISIBLE

            if (firstGroupFunctions?.isVisible == true) {
                firstGroupFunctions.visibility = INVISIBLE
            } else if (secondGroupFunctions?.isVisible == true) {
                secondGroupFunctions.visibility = INVISIBLE

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
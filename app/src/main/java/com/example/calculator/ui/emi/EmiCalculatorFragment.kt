package com.example.calculator.ui.emi

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.calculator.R
import com.example.calculator.databinding.FragmentEmiCalculatorBinding
import com.example.calculator.utils.parseDouble
import com.example.calculator.utils.viewBinding
import kotlinx.coroutines.launch


class EmiCalculatorFragment : Fragment(R.layout.fragment_emi_calculator) {

    private val emiViewModel : EMIViewModel by activityViewModels()
    private val fragmentEmiCalculatorBinding by viewBinding(FragmentEmiCalculatorBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                emiViewModel.emiCalculatorState.collect { emiCalculatorState ->
                    when {
                        emiCalculatorState.isFirstEmiCalculator -> showFirstEmiCalculator()
                        emiCalculatorState.isSecondEmiCalculator -> showSecondEmiCalculator()
                    }
                }
            }
        }

        fragmentEmiCalculatorBinding.apply {

            emiCalculatorAction.setOnClickListener {

                val loanAmount = parseDouble(loanAmount.text.toString())
                val numberInstallments = parseDouble(numberInstallments.text.toString())
                val interestRate = parseDouble(interestRate.text.toString())
                val isFirstEmiCalculator = emiViewModel.emiCalculatorState.value.isFirstEmiCalculator
                val isSecondEmiCalculator = emiViewModel.emiCalculatorState.value.isSecondEmiCalculator

                emiViewModel.calculateEmi(loanAmount , interestRate, numberInstallments)

                when {
                    isFirstEmiCalculator -> navigateToEmiCalculation()
                    isSecondEmiCalculator -> navigateToCompareFragment()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setOrientationPortraitMode()
    }

    private fun showFirstEmiCalculator() {
        fragmentEmiCalculatorBinding.labelSecondEmiCalculator.visibility = View.GONE
        fragmentEmiCalculatorBinding.emiCalculatorAction.text = "Calculate"
    }

    private fun showSecondEmiCalculator() {
        fragmentEmiCalculatorBinding.labelSecondEmiCalculator.visibility = View.VISIBLE
        fragmentEmiCalculatorBinding.emiCalculatorAction.text = "Compare"
    }

    private fun navigateToEmiCalculation() {
        val action = EmiCalculatorFragmentDirections.actionEmiCalculatorToEmiCalculation()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun navigateToCompareFragment() {
        val action = EmiCalculatorFragmentDirections.actionEmiCalculatorToCompareFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun setOrientationPortraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }



}
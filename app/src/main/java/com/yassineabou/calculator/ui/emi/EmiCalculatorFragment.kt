package com.yassineabou.calculator.ui.emi

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.yassineabou.calculator.R
import com.yassineabou.calculator.databinding.FragmentEmiCalculatorBinding
import com.yassineabou.calculator.util.parseDouble
import com.yassineabou.calculator.util.viewBinding
import kotlinx.coroutines.launch

class EmiCalculatorFragment : Fragment(R.layout.fragment_emi_calculator) {

    private val emiViewModel: EMIViewModel by activityViewModels()
    private val fragmentEmiCalculatorBinding by viewBinding(FragmentEmiCalculatorBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                emiViewModel.emiCalculatorState.collect { emiCalculatorState ->
                    when {
                        emiCalculatorState.isFirstEmiCalculator -> displaySecondEmiLabel()
                        emiCalculatorState.isSecondEmiCalculator -> hideSecondEmiLabel()
                    }
                }
            }
        }

        fragmentEmiCalculatorBinding.apply {
            emiCalculatorAction.setOnClickListener {
                val loanAmount = loanAmount.text.toString().parseDouble()
                val numberInstallments = numberInstallments.text.toString().parseDouble()
                val interestRate = interestRate.text.toString().parseDouble()

                emiViewModel.calculateEmi(loanAmount, interestRate, numberInstallments)

                if (emiViewModel.emiCalculatorState.value.isSecondEmiCalculator) {
                    navigateToCompareFragment()
                } else {
                    navigateToEmiCalculation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setOrientationPortraitMode()
    }

    private fun displaySecondEmiLabel() {
        fragmentEmiCalculatorBinding.labelSecondEmiCalculator.visibility = View.GONE
        fragmentEmiCalculatorBinding.emiCalculatorAction.text = getString(R.string.calculate)
    }

    private fun hideSecondEmiLabel() {
        fragmentEmiCalculatorBinding.labelSecondEmiCalculator.visibility = View.VISIBLE
        fragmentEmiCalculatorBinding.emiCalculatorAction.text = getString(R.string.compare)
    }

    private fun navigateToCompareFragment() {
        val action = EmiCalculatorFragmentDirections.emiCalculatorToCompareFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun navigateToEmiCalculation() {
        val action = EmiCalculatorFragmentDirections.emiCalculatorToEmiCalculation()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun setOrientationPortraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}

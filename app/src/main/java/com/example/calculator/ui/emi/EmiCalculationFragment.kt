package com.example.calculator.ui.emi

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.calculator.R
import com.example.calculator.databinding.FragmentEmiCalculationBinding
import com.example.calculator.util.viewBinding

class EmiCalculationFragment : Fragment(R.layout.fragment_emi_calculation) {

    private val fragmentEmiCalculationBinding by viewBinding(FragmentEmiCalculationBinding::bind)
    private val emiViewModel: EMIViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentEmiCalculationBinding.lifecycleOwner = viewLifecycleOwner
        fragmentEmiCalculationBinding.viewModel = emiViewModel

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.share_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.share -> {
                            shareEmiCalculationResult()
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner, Lifecycle.State.RESUMED
        )

        fragmentEmiCalculationBinding.apply {

            compare.setOnClickListener {
                navigateToEmiCalculator()
            }

            done.setOnClickListener {
                navigateToCalculator()
            }
        }
    }

    private fun navigateToEmiCalculator() {
        emiViewModel.changeEmiCalculatorState(
            EmiCalculatorState(isFirstEmiCalculator = false, isSecondEmiCalculator = true)
        )
        val action = EmiCalculationFragmentDirections.actionEmiCalculationToEmiCalculator()
        NavHostFragment.findNavController(this@EmiCalculationFragment).navigate(action)
    }

    private fun navigateToCalculator() {
        emiViewModel.changeEmiCalculatorState(
            EmiCalculatorState(isFirstEmiCalculator = false, isSecondEmiCalculator = false)
        )
        val action = EmiCalculationFragmentDirections.actionEmiCalculationToCalculatorFragment()
        NavHostFragment.findNavController(this@EmiCalculationFragment).navigate(action)
    }

    override fun onResume() {
        super.onResume()
        setOrientationPortraitMode()
    }

    private fun setOrientationPortraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun shareEmiCalculationResult() {
        startActivity(getShareIntent())
    }

    private fun getShareIntent(): Intent {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                emiViewModel.firstEmiCalculation.value?.let {
                    getString(
                        R.string.shareEmiCalculation, it.principal, it.emiAmount,
                        it.interestRate, it.numberInstallments
                    )
                }
            )
        }

        return Intent.createChooser(shareIntent, null)
    }
}

package com.example.calculator0.ui.emi


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
import com.example.calculator0.R
import com.example.calculator0.databinding.FragmentEmiCalculationBinding
import com.example.calculator0.utils.viewBinding


class EmiCalculationFragment : Fragment(R.layout.fragment_emi_calculation) {

    private val emiViewModel : EMIViewModel by activityViewModels()
    private val binding by viewBinding(FragmentEmiCalculationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emiViewModel = emiViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.share_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.share -> {
                        shareSuccess()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.apply {

            emiViewModel?.let { emiViewModel ->
                compare.setOnClickListener {
                    emiViewModel.changeEmiCalculatorState(
                        EmiCalculatorState(isFirstEmiCalculator = false, isSecondEmiCalculator = true)
                    )
                    val action = EmiCalculationFragmentDirections.actionEmiCalculationToEmiCalculator()
                    NavHostFragment.findNavController(this@EmiCalculationFragment).navigate(action)
                }

                done.setOnClickListener {
                    emiViewModel.changeEmiCalculatorState(
                        EmiCalculatorState(isFirstEmiCalculator = false, isSecondEmiCalculator = false)
                    )
                    val action = EmiCalculationFragmentDirections.actionEmiCalculationToCalculatorFragment()
                    NavHostFragment.findNavController(this@EmiCalculationFragment).navigate(action)
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        setOrientationPortraitMode()
    }

    private fun setOrientationPortraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun getShareIntent() : Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT,
                emiViewModel.firstEmiCalculation.value?.let {
                    getString(R.string.shareEmiCalculation, it.principal, it.emiAmount,
                        it.interestRate, it.numberInstallments)
                 }
            )
        return shareIntent
    }


    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

}


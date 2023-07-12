package com.yassineabou.calculator.ui.emi

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
import com.yassineabou.calculator.R
import com.yassineabou.calculator.databinding.FragmentCompareBinding
import com.yassineabou.calculator.util.safeLet
import com.yassineabou.calculator.util.viewBinding

class CompareFragment : Fragment(R.layout.fragment_compare) {

    private val fragmentCompareBinding by viewBinding(FragmentCompareBinding::bind)
    private val emiViewModel: EMIViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentCompareBinding.lifecycleOwner = viewLifecycleOwner
        fragmentCompareBinding.viewModel = emiViewModel
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.share_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.share -> {
                            shareCompareResult()
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED,
        )

        fragmentCompareBinding.apply {
            done.setOnClickListener {
                navigateToCalculator()
            }

            reset.setOnClickListener {
                navigateToEmiCalculator()
            }
        }
    }

    private fun navigateToCalculator() {
        emiViewModel.updateEmiCalculatorState(
            EmiCalculatorState(isFirstEmiCalculator = false, isSecondEmiCalculator = false),
        )
        val action = CompareFragmentDirections.compareFragmentToCalculatorFragment()
        NavHostFragment.findNavController(this@CompareFragment).navigate(action)
    }

    private fun navigateToEmiCalculator() {
        emiViewModel.updateEmiCalculatorState(
            EmiCalculatorState(isFirstEmiCalculator = true, isSecondEmiCalculator = false),
        )
        val action = CompareFragmentDirections.compareFragmentToEmiCalculator()
        NavHostFragment.findNavController(this@CompareFragment).navigate(action)
    }

    override fun onResume() {
        super.onResume()
        setOrientationPortraitMode()
    }

    private fun setOrientationPortraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun shareCompareResult() {
        startActivity(getShareIntent())
    }

    private fun getShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)

        safeLet(
            emiViewModel.firstEmiCalculation.value,
            emiViewModel.secondEmiCalculation.value,
        ) { firstEmiCalculation, secondEmiCalculation ->

            shareIntent.setType("text/plain")
                .putExtra(
                    Intent.EXTRA_TEXT,
                    getString(
                        R.string.shareCompare,
                        firstEmiCalculation.principal,
                        firstEmiCalculation.emiAmount,
                        firstEmiCalculation.interestRate,
                        firstEmiCalculation.numberInstallments,
                        secondEmiCalculation.principal,
                        secondEmiCalculation.emiAmount,
                        secondEmiCalculation.interestRate,
                        secondEmiCalculation.numberInstallments,
                    ),
                )
        }

        return Intent.createChooser(shareIntent, null)
    }
}

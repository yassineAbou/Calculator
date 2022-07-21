package com.example.calculator.ui.emi

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.calculator.R
import com.example.calculator.databinding.FragmentCompareBinding
import com.example.calculator.utils.safeLet
import com.example.calculator.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompareFragment : Fragment(R.layout.fragment_compare) {

    private val emiViewModel : EMIViewModel by activityViewModels()
    private val binding by viewBinding(FragmentCompareBinding::bind)

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
                done.setOnClickListener {
                    emiViewModel.changeEmiCalculatorState(
                        EmiCalculatorState(isFirstEmiCalculator = false, isSecondEmiCalculator = false)
                    )
                    val action = CompareFragmentDirections.actionCompareFragmentToCalculatorFragment()
                    NavHostFragment.findNavController(this@CompareFragment).navigate(action)

                }

                reset.setOnClickListener {
                    emiViewModel.changeEmiCalculatorState(
                        EmiCalculatorState(isFirstEmiCalculator = true, isSecondEmiCalculator = false)
                    )
                    val action = CompareFragmentDirections.actionCompareFragmentToEmiCalculator()
                    NavHostFragment.findNavController(this@CompareFragment).navigate(action)
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
        safeLet(
            emiViewModel.firstEmiCalculation.value,
            emiViewModel.secondEmiCalculation.value
        ) { firstEmiCalculation, secondEmiCalculation ->

                shareIntent.setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.shareCompare, firstEmiCalculation.principal, firstEmiCalculation.emiAmount,
                            firstEmiCalculation.interestRate, firstEmiCalculation.numberInstallments, secondEmiCalculation.principal,
                            secondEmiCalculation.emiAmount, secondEmiCalculation.interestRate,
                            secondEmiCalculation.numberInstallments)
                    )
        }

        return shareIntent

    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

}
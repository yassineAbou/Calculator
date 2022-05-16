package com.example.calculator0.ui.emi

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.calculator0.databinding.FragmentEmiTwoBinding
import com.example.calculator0.utils.parseDouble


class ThreeFragment : Fragment() {


    private lateinit var binding: FragmentEmiTwoBinding
    private val emiViewModel : EMIViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentEmiTwoBinding.inflate(inflater, container, false)

        portraitMode()

        setHasOptionsMenu(true)
        binding.apply {

            compare.setOnClickListener {

                val secondLoanAmount = parseDouble(loanAmount2.text.toString())
                val secondNumberOfInstallments = parseDouble(numberOfInstallments2.text.toString())
                val secondInterestRate = parseDouble(interestRate2.text.toString())

                emiViewModel.emi(secondLoanAmount , secondInterestRate, secondNumberOfInstallments, EmiScreen.Second)

                it.findNavController()
                    .navigate(ThreeFragmentDirections.actionEmiTwoFragmentToCompareFragment())
            }
        }

        return binding.root
    }

    private fun portraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }



}
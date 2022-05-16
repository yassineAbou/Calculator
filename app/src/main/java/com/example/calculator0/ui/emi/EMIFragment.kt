package com.example.calculator0.ui.emi

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.calculator0.databinding.FragmentEmiBinding
import com.example.calculator0.utils.parseDouble


class EMIFragment : Fragment() {

    private val emiViewModel : EMIViewModel by activityViewModels()
    private lateinit var binding: FragmentEmiBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmiBinding.inflate(inflater, container, false)


        portraitMode()


        binding.apply {

            calculate.setOnClickListener {

                val loanAmount = parseDouble(loanAmount.text.toString())
                val numberOfInstallments = parseDouble(numberOfInstallments.text.toString())
                val interestRate = parseDouble(interestRate.text.toString())

                emiViewModel.emi(loanAmount, interestRate, numberOfInstallments, EmiScreen.First)

                it.findNavController()
                    .navigate(EMIFragmentDirections.actionEmiOneFragmentToResultFragment())

            }
        }
        
        return binding.root
    }



    private fun portraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }





}

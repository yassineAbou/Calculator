package com.example.calculator0

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.calculator0.databinding.FragmentEmiTwoBinding
import com.example.calculator0.ui.emi.EMIViewModel
import com.example.calculator0.ui.emi.EmiScreen
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

                val P = parseDouble(loanAmount2.text.toString())
                val N = parseDouble(numberOfInstallments2.text.toString())
                val I = parseDouble(interestRate2.text.toString())

                emiViewModel.emi(P, I, N, EmiScreen.Second)

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
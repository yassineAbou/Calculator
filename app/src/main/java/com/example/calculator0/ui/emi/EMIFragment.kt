package com.example.calculator0.ui.emi

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import com.example.calculator0.R
import com.example.calculator0.databinding.FragmentEmiBinding
import java.math.RoundingMode
import java.text.DecimalFormat


class EMIFragment : Fragment() {

    private lateinit var binding: FragmentEmiBinding
    private var emiMonth = ""
    private var interest = ""
    private var total = ""
    private var principal = ""
    private var installment = ""
    private var interestRateOutput = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmiBinding.inflate(inflater, container, false)

        //setHasOptionsMenu(true)

        //showActionBar()
        portraitMode()


        binding.apply {

            calculate.setOnClickListener {

                val P = parseDouble(loanAmount.text.toString())
                val N = parseDouble(numberOfInstallments.text.toString())
                val I = parseDouble(interestRate.text.toString())

                emi(P, I, N)

                //pass data to CompareFragment
                setFragmentResult("requestKey", bundleOf("firstLoan" to principal,
                    "firstEmi" to emiMonth, "firstInstallments" to installment, "firstTotal" to total,
                     "interestOptionOne" to interest, "firstInterest" to interestRateOutput))

                it.findNavController()
                    .navigate(EMIFragmentDirections.actionEmiOneFragmentToResultFragment(
                        emiMonth, installment, principal, interest, total, interestRateOutput))

            }
        }
        
        return binding.root
    }



    private fun portraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun parseDouble(`val`: String?): Double {
        return if (`val` == null || `val`.isEmpty()) 0.0 else `val`.toDouble()
    }

    private fun emi(P: Double, I: Double, N: Double) {

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        val Month = N
        val InterestValue = I / 12 / 100
        val CommonPart = Math.pow(1 + InterestValue, Month)
        val DivUp = (P * InterestValue * CommonPart)
        val DivDown = CommonPart - 1
        val emiCalculationPerMonth: Float = ((DivUp / DivDown)).toFloat()
        emiCalculationPerMonth * 12
        val totalInterest = (emiCalculationPerMonth * Month) - P
        val totalPayment = totalInterest + P

        emiMonth = df.format(emiCalculationPerMonth)
        interest = df.format(totalInterest)
        total = df.format(totalPayment)
        principal =  df.format(P)
        installment = df.format(N)
        interestRateOutput = df.format(I)
    }

}
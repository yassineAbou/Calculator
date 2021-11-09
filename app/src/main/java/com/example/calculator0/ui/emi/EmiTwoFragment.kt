package com.example.calculator0

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.calculator0.databinding.FragmentEmiTwoBinding
import java.math.RoundingMode
import java.text.DecimalFormat


class ThreeFragment : Fragment() {


    private lateinit var binding: FragmentEmiTwoBinding


    private var principal2 = ""
    private var installment2 = ""
    private var interest2 = ""
    private var emiMonth2 = ""
    private var total2 = ""
    private var interestRateOutput2 = ""

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

                emi(P, I, N)

                it.findNavController()
                    .navigate(ThreeFragmentDirections.actionEmiTwoFragmentToCompareFragment(
                    principal2,installment2,interest2,emiMonth2,total2,interestRateOutput2))
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

        installment2 = df.format(N)
        principal2 =  df.format(P)
        interest2 = df.format(totalInterest)
        emiMonth2 = df.format(emiCalculationPerMonth)
        total2 = df.format(totalPayment)
        interestRateOutput2 = df.format(I)

    }

}
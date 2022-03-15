package com.example.calculator0.ui.emi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

enum class EmiScreen { First, Second }

class EMIViewModel : ViewModel() {

    private val _emiFirstResult: MutableStateFlow<Emi?> = MutableStateFlow(null)
    val emiFirstResult = _emiFirstResult.asStateFlow()

    private val _emiSecondResult: MutableStateFlow<Emi?> = MutableStateFlow(null)
    val emiSecondResult = _emiSecondResult.asStateFlow()

     fun emi(P: Double, I: Double, N: Double, emiScreen: EmiScreen) {

         viewModelScope.launch(Dispatchers.IO) {
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

             val  emiMonth = df.format(emiCalculationPerMonth)
             val  interest = df.format(totalInterest)
             val  total = df.format(totalPayment)
             val  principal =  df.format(P)
             val  installment = df.format(N)
             val  interestRateOutput = df.format(I)

             val result = Emi(emiMonth, interest,  I.toString(), total, principal, installment, interestRateOutput)

             when (emiScreen) {
                 EmiScreen.First -> _emiFirstResult.value = result
                 EmiScreen.Second -> _emiSecondResult.value = result
             }
         }

    }
}

data class Emi(
    val  emiMonth: String,
    val  interest: String,
    val  interestRate: String,
    val  total: String,
    val  principal: String,
    val  installment: String,
    val  interestRateOutput: String
)
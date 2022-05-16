package com.example.calculator0.ui.emi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

enum class EmiScreen { First, Second }

class EMIViewModel : ViewModel() {

    private val _emiFirstResult: MutableStateFlow<Emi?> = MutableStateFlow(null)
    val emiFirstResult = _emiFirstResult.asStateFlow()

    private val _emiSecondResult: MutableStateFlow<Emi?> = MutableStateFlow(null)
    val emiSecondResult = _emiSecondResult.asStateFlow()

     fun emi(loanAmount: Double, interestRate: Double, numberOfInstallments: Double, emiScreen: EmiScreen) {

         viewModelScope.launch(Dispatchers.IO) {
             val df = DecimalFormat("#.##")
             df.roundingMode = RoundingMode.CEILING

             val interestValue = interestRate / 12 / 100
             val commonPart = (1 + interestValue).pow(numberOfInstallments)
             val divUp = (loanAmount * interestValue * commonPart)
             val divDown = commonPart - 1
             val emiCalculationPerMonth: Float = ((divUp / divDown)).toFloat()
             emiCalculationPerMonth * 12
             val totalInterest = (emiCalculationPerMonth * numberOfInstallments) - loanAmount
             val totalPayment = totalInterest + loanAmount

             val  emiMonth = df.format(emiCalculationPerMonth)
             val  interest = df.format(totalInterest)
             val  total = df.format(totalPayment)
             val  principal =  df.format(loanAmount)
             val  installment = df.format(numberOfInstallments)
             val  interestRateOutput = df.format(interestRate)

             val result = Emi(emiMonth, interest,  interestRate.toString(), total, principal, installment, interestRateOutput)

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
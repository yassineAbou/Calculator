package com.yassineabou.calculator.ui.emi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yassineabou.calculator.util.decimalFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.pow

data class EmiCalculatorState(
    val isFirstEmiCalculator: Boolean = true,
    val isSecondEmiCalculator: Boolean = false,
)

data class Emi(
    val emiAmount: String,
    val interest: String,
    val interestRate: String,
    val totalAmount: String,
    val principal: String,
    val numberInstallments: String,
)

class EMIViewModel : ViewModel() {

    private val _emiCalculatorState = MutableStateFlow(EmiCalculatorState())
    val emiCalculatorState: StateFlow<EmiCalculatorState> = _emiCalculatorState.asStateFlow()

    private val _firstEmiCalculation: MutableStateFlow<Emi?> = MutableStateFlow(null)
    val firstEmiCalculation = _firstEmiCalculation.asStateFlow()

    private val _secondEmiCalculation: MutableStateFlow<Emi?> = MutableStateFlow(null)
    val secondEmiCalculation = _secondEmiCalculation.asStateFlow()

    fun updateEmiCalculatorState(emiCalculatorState: EmiCalculatorState) {
        _emiCalculatorState.value = emiCalculatorState
    }

    fun calculateEmi(loanAmount: Double, interestRate: Double, numberInstallments: Double) {
        viewModelScope.launch {
            val interestValue = interestRate / 12 / 100
            val commonPart = (1 + interestValue).pow(numberInstallments)
            val divUp = (loanAmount * interestValue * commonPart)
            val divDown = commonPart - 1
            val emiCalculationPerMonth: Float = ((divUp / divDown)).toFloat()
            emiCalculationPerMonth * 12
            val totalInterest = (emiCalculationPerMonth * numberInstallments) - loanAmount
            val totalPayment = totalInterest + loanAmount

            val emi = Emi(
                emiAmount = emiCalculationPerMonth.decimalFormat(),
                interest = totalInterest.decimalFormat(),
                interestRate = interestRate.decimalFormat(),
                totalAmount = totalPayment.decimalFormat(),
                principal = loanAmount.decimalFormat(),
                numberInstallments = numberInstallments.decimalFormat(),
            )

            if (_emiCalculatorState.value.isSecondEmiCalculator) {
                _secondEmiCalculation.value = emi
            } else {
                _firstEmiCalculation.value = emi
            }
        }
    }
}

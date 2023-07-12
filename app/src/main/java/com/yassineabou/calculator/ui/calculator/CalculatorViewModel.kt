package com.yassineabou.calculator.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yassineabou.calculator.data.model.PreviousOperation
import com.yassineabou.calculator.data.repository.PreviousOperationRepository
import com.yassineabou.calculator.util.isBalancedBrackets
import com.yassineabou.calculator.util.safeLet
import com.yassineabou.calculator.util.trimTrailingZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mariuszgromada.math.mxparser.Expression
import javax.inject.Inject

data class CalculatorState(
    val isInvalidFormat: Boolean = false,
    val isFirstGroupFunctions: Boolean = true,
    val isSecondGroupFunctions: Boolean = false,
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val previousOperationRepository: PreviousOperationRepository,
) : ViewModel() {

    val listPreviousOperationsFlow = previousOperationRepository.listPreviousOperationsFlow
    private val listChars: List<Char> = listOf(')', 'e', 'i', '%')
    private val listArithmeticSymbols: List<Char> = listOf('+', '-', '×', '÷', '.')
    private val listNumbers: List<Char> =
        listOf(')', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '%', 'e', 'i', 'ℼ')
    private var isDecimalPointClicked = true
    private var arithmeticSymbol = '+'

    private val _input: MutableStateFlow<String?> = MutableStateFlow(null)
    val input = _input.asStateFlow()

    private val _result: MutableStateFlow<String?> = MutableStateFlow(null)
    val result = _result.asStateFlow()

    private val _calculatorState = MutableStateFlow(CalculatorState())
    val calculatorState: StateFlow<CalculatorState> = _calculatorState.asStateFlow()

    init {
        _input.value = ""
        _result.value = ""
    }

    fun updateInput(input: String) {
        _input.value = input
    }

    fun updateResult(result: String) {
        _result.value = result
    }

    fun updateCalculatorState(calculatorState: CalculatorState) {
        viewModelScope.launch {
            _calculatorState.value = calculatorState
        }
    }

    fun appendNumberFromHistory(number: String) {
        val inputAfterArithmetic = _input.value?.substringAfterLast(arithmeticSymbol)
        inputAfterArithmetic?.let {
            when {
                inputAfterArithmetic == "0" -> {
                    _input.value = _input.value?.dropLast(1) + number
                }
                (inputAfterArithmetic.lastOrNull() in listChars) && !inputAfterArithmetic.contains('.') -> {
                    _input.value = "${_input.value}×$number"
                }
                (inputAfterArithmetic.lastOrNull() != '%') && !inputAfterArithmetic.contains('.') -> {
                    _input.value += number
                }
                else -> {
                    updateCalculatorState(CalculatorState(isInvalidFormat = true))
                }
            }
        }
    }

    fun appendFunction(function: String) {
        _input.value?.let { input ->
            _input.value = when {
                input.lastOrNull() in listNumbers -> ("$input×$function")
                else -> ("$input$function")
            }
        }
    }

    fun appendPower(power: String) {
        _input.value?.let { input ->
            val inputAfterArithmetic = input.substringAfterLast(arithmeticSymbol)
            if (inputAfterArithmetic.isNotEmpty() && (inputAfterArithmetic.lastOrNull() in listNumbers)) {
                _input.value = ("$input$power")
            } else {
                updateCalculatorState(CalculatorState(isInvalidFormat = true))
            }
        }
    }

    fun calculateInput() {
        _input.value?.let { input ->
            viewModelScope.launch(Dispatchers.IO) {
                isDecimalPointClicked = true
                input.substringAfterLast(arithmeticSymbol)

                val expression = Expression(input.replace('×', '*').replace('÷', '/'))
                val output =
                    trimTrailingZero(java.lang.String.valueOf(expression.calculate().toFloat()))
                _result.value =
                    if (input.lastOrNull() in listOf('+', '-') || output == "NaN") "" else output
            }
        }
    }

    fun removeLastInputChar() {
        _input.value?.let {
            if (it != "") {
                _input.value = it.substring(0, it.length - 1)
            }
        }
    }

    fun appendDecimalPoint() {
        _input.value?.let { input ->
            val inputAfterArithmetic = input.substringAfterLast(arithmeticSymbol)
            if (inputAfterArithmetic.isEmpty() || inputAfterArithmetic.lastOrNull() == '(' && isDecimalPointClicked) {
                _input.value = (input + "0.")
                isDecimalPointClicked = false
            }
            if (isDecimalPointClicked && (inputAfterArithmetic.lastOrNull()?.isDigit() == true) && inputAfterArithmetic.none { char -> char == '.' }
            ) {
                _input.value = ("$input.")
                isDecimalPointClicked = false
            }
        }
    }

    fun clearInput() {
        _input.value = ""
        _result.value = ""
    }

    fun appendMinusSign() {
        _input.value?.let { input ->
            _input.value = if (input.lastOrNull() in listNumbers) ("$input×(-") else ("$input(-")
        }
    }

    fun appendBrackets() {
        _input.value?.let { input ->
            _input.value = when {
                isBalancedBrackets(input) && input.lastOrNull() in listNumbers -> ("$input×(")
                isBalancedBrackets(input) -> ("$input(")
                else -> ("$input)")
            }
        }
    }

    fun insert() {
        safeLet(_input.value, _result.value) { input, result ->
            if (result.isNotEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    val previousOperation = PreviousOperation(input, result)
                    previousOperationRepository.insert(previousOperation)
                    _input.value = result
                    _result.value = ""
                }
            }
        }
    }

    fun appendPercentage() {
        _input.value?.let { input ->
            val inputAfterArithmetic = input.substringAfterLast(arithmeticSymbol)
            if (inputAfterArithmetic.isNotEmpty() && (inputAfterArithmetic.lastOrNull() in listNumbers) && (inputAfterArithmetic.lastOrNull() != '%')) {
                _input.value = ("$input%")
                isDecimalPointClicked = true
            } else {
                updateCalculatorState(CalculatorState(isInvalidFormat = true))
            }
        }
    }

    fun appendNumber(number: String) {
        _input.value?.let { input ->
            val inputAfterArithmetic = input.substringAfterLast(arithmeticSymbol)

            _input.value = when (inputAfterArithmetic) {
                "0", "(0", "(-0" -> input.dropLast(1) + number
                else -> when (inputAfterArithmetic.lastOrNull()) {
                    in listChars -> "$input×$number"
                    else -> input + number
                }
            }
        }
    }

    fun appendArithmetic(arithmeticSymbol: String) {
        _input.value?.let { input ->
            this.arithmeticSymbol = arithmeticSymbol.single()

            if (input.lastOrNull() !in listArithmeticSymbols && input.isNotEmpty()) {
                _input.value = (input + arithmeticSymbol)
                isDecimalPointClicked = true
            } else if (input.lastOrNull() in listArithmeticSymbols) {
                _input.value = (input.dropLast(1) + arithmeticSymbol)
                isDecimalPointClicked = true
            }
        }
    }

    fun clearListPreviousOperations() = viewModelScope.launch(Dispatchers.IO) {
        previousOperationRepository.clear()
    }
}

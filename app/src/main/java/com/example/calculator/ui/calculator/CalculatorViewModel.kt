package com.example.calculator.ui.calculator

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.data.model.PreviousOperation
import com.example.calculator.data.repository.PreviousOperationRepository
import com.example.calculator.util.isBalancedBrackets
import com.example.calculator.util.safeLet
import com.example.calculator.util.trimTrailingZero
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mariuszgromada.math.mxparser.Expression

data class CalculatorState(
    val isInvalidFormat: Boolean = false,
    val isFirstGroupFunctions: Boolean = false,
    val isSecondGroupFunctions: Boolean = false,
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val previousOperationRepository: PreviousOperationRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val listPreviousOperationsFlow = previousOperationRepository.listPreviousOperationsFlow
    private val listChars: List<Char> = listOf(')', 'e', 'i', '%')
    private val listArithmeticSymbols: List<Char> = listOf('+', '-', '×', '÷', '.')
    private val listNumbers: List<Char> = listOf(')', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '%', 'e', 'i', 'ℼ')
    private var isDecimalPointClicked = true
    private var arithmeticOperation = '+'

    private val _currentInput: MutableStateFlow<String?> = MutableStateFlow(null)
    val currentInput = _currentInput.asStateFlow()

    private val _result: MutableStateFlow<String?> = MutableStateFlow(null)
    val result = _result.asStateFlow()

    private val _calculatorState = MutableStateFlow(CalculatorState())
    val calculatorState: StateFlow<CalculatorState> = _calculatorState.asStateFlow()

    init {
        _currentInput.value = ""
        _result.value = ""
    }

    fun changeCurrentInput(input: String) {
        _currentInput.value = input
    }

    fun changeResult(result: String) {
        _result.value = result
    }

    fun changeCalculatorState(calculatorState: CalculatorState) {
        viewModelScope.launch {
            _calculatorState.value = calculatorState
        }
    }

    fun addToCurrentInput(number: String) {
        val input = _currentInput.value?.substringAfterLast(arithmeticOperation)
        input?.let {
            when {
                it == "0" -> _currentInput.value = (_currentInput.value?.dropLast(1) + number)
                it.lastOrNull() in listChars && !it.contains('.') -> _currentInput.value = ("${_currentInput.value}×$number")
                (it.lastOrNull() != '%') && !it.contains('.') -> _currentInput.value = (_currentInput.value + number)
                else -> changeCalculatorState(CalculatorState(isInvalidFormat = true))
            }
        }
    }

    fun addFunction(function: String) {
        _currentInput.value?.let {
            _currentInput.value = when {
                it.lastOrNull() in listNumbers -> ("$it×$function")
                else -> ("$it$function")
            }
        }
    }

    fun addPower(power: String) {
        _currentInput.value?.let {
            val afterArithmeticOperation = it.substringAfterLast(arithmeticOperation)
            if (afterArithmeticOperation.isNotEmpty() && (afterArithmeticOperation.lastOrNull() in listNumbers)) {
                _currentInput.value = ("$it$power")
            } else {
                changeCalculatorState(CalculatorState(isInvalidFormat = true))
            }
        }
    }

    fun calculateCurrentInput() {
        _currentInput.value?.let {
            savedStateHandle["currentInput"] = it
            viewModelScope.launch(Dispatchers.IO) {
                val afterArithmeticOperation = it.substringAfterLast(arithmeticOperation)

                val input = it.replace('×', '*')
                    .replace('÷', '/')
                val expression = Expression(input)
                val output = trimTrailingZero(java.lang.String.valueOf(expression.calculate().toFloat()))
                _result.value = if (it.lastOrNull() in listOf('+', '-') || output == "NaN") "" else output

                if (afterArithmeticOperation.isDigitsOnly())
                    isDecimalPointClicked = true

                if (afterArithmeticOperation.isEmpty()) {
                    isDecimalPointClicked = true
                }
            }
        }
    }

    fun removeCurrentInputChars() {
        _currentInput.value?.let {
            if (it != "") {
                _currentInput.value = it.substring(0, it.length - 1)
            }
        }
    }

    fun addDecimalPoint() {
        _currentInput.value?.let {
            val afterArithmeticOperation = it.substringAfterLast(arithmeticOperation)
            if ((afterArithmeticOperation.isEmpty()) && isDecimalPointClicked) {
                _currentInput.value = (it + "0.")
                isDecimalPointClicked = false
            }
            if (isDecimalPointClicked && (afterArithmeticOperation.lastOrNull()?.isDigit() == true) && afterArithmeticOperation.none { char -> char == '.' }) {
                _currentInput.value = ("$it.")
                isDecimalPointClicked = false
            }
        }
    }

    fun clearCurrentInput() {
        _currentInput.value = ""
        _result.value = ""
    }

    fun addMinusSign() {
        _currentInput.value?.let {
            _currentInput.value = if (it.lastOrNull() in listNumbers) ("$it×(-") else ("$it(-")
        }
    }

    fun addBrackets() {
        _currentInput.value?.let {
            _currentInput.value = when {
                isBalancedBrackets(it) && it.lastOrNull() in listNumbers -> ("$it×(")
                isBalancedBrackets(it) -> ("$it(")
                else -> ("$it)")
            }
        }
    }

    fun insert() {
        safeLet(_currentInput.value, _result.value) { currentInputValue, resultValue ->
            if (resultValue.isNotEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    val previousOperation = PreviousOperation(currentInputValue, resultValue)
                    previousOperationRepository.insert(previousOperation)
                    _currentInput.value = resultValue
                    _result.value = ""
                }
            }
        }
    }

    fun addPercentage() {
        _currentInput.value?.let {
            val afterArithmeticOperation = it.substringAfterLast(arithmeticOperation)
            if (afterArithmeticOperation.isNotEmpty() && (afterArithmeticOperation.lastOrNull() in listNumbers) && (afterArithmeticOperation.lastOrNull() != '%')) {
                _currentInput.value = ("$it%")
                isDecimalPointClicked = true
            } else {
                changeCalculatorState(CalculatorState(isInvalidFormat = true))
            }
        }
    }

    fun addNumber(number: String) {
        _currentInput.value?.let {
            val afterArithmeticOperation = it.substringAfterLast(arithmeticOperation)
            when {
                equals("0") -> _currentInput.value = (it.dropLast(1) + number)
                afterArithmeticOperation.lastOrNull() in listChars -> _currentInput.value = ("$it×$number")
                else -> _currentInput.value = (it + number)
            }
        }
    }

    fun addArithmeticOperation(arithmeticSymbol: String) {
        _currentInput.value?.let {
            arithmeticOperation = arithmeticSymbol.single()
            if (it.lastOrNull() !in listArithmeticSymbols && it.isNotEmpty()) {
                _currentInput.value = (it + arithmeticSymbol)
                isDecimalPointClicked = true
            } else if (it.lastOrNull() in listArithmeticSymbols) {
                _currentInput.value = (it.dropLast(1) + arithmeticSymbol)
                isDecimalPointClicked = true
            }
        }
    }

    fun clearListPreviousOperations() = viewModelScope.launch(Dispatchers.IO) {
        previousOperationRepository.clear()
    }
}

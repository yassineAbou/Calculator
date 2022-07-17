package com.example.calculator0.ui.calculator




import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator0.database.PrevOperation
import com.example.calculator0.repository.PrevOperationRepository
import com.example.calculator0.utils.isBalancedBrackets
import com.example.calculator0.utils.safeLet
import com.example.calculator0.utils.trimTrailingZero
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
    val isFirstGroupFunctions: Boolean = false,
    val isSecondGroupFunctions: Boolean = false,
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val prevOperationRepository: PrevOperationRepository
) : ViewModel() {


    val listPrevOperations = prevOperationRepository.listPrevOperations
    private val listChars: List<Char> = listOf(')','e', 'i')
    private val listArithmeticSymbols: List<Char> = listOf('+','-','×','÷','.')
    private val listNumbers: List<Char> = listOf(')','1','2','3','4','5','6','7','8','9','0','%','e','i')
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

    fun onChangeCurrentInput(input: String) {
        _currentInput.value = input
    }

    fun onChangeResult(result: String) {
        _result.value = result
    }

    fun onChangeCalculatorState(calculatorState: CalculatorState) {
        viewModelScope.launch {
            _calculatorState.value = calculatorState
        }
    }

    fun addToCurrentInput(number: String) {
        val input = _currentInput.value?.substringAfterLast(arithmeticOperation)
        input?.let{
            when {
                it == "0" ->  _currentInput.value = (_currentInput.value?.dropLast(1) + number)
                it.lastOrNull() in listChars && !it.contains('.')  -> _currentInput.value =  ("${_currentInput.value}×$number")
                (it.lastOrNull() != '%') && !it.contains('.') -> _currentInput.value =  (_currentInput.value +  number)
                else  -> onChangeCalculatorState(CalculatorState(isInvalidFormat = true))
            }
        }
    }

    fun addFunctions(function: String) {
        _currentInput.value?.let {
            _currentInput.value = with(it) {
                when {
                    lastOrNull() in listNumbers -> ("$this×$function")
                    else -> ("$this$function")
                }
            }
        }
    }


    fun addPower(power: String) {
        _currentInput.value?.let {
            val afterArithmeticOperation = it.substringAfterLast(arithmeticOperation)
            if (afterArithmeticOperation.isNotEmpty() && (afterArithmeticOperation.lastOrNull() in listNumbers)) {
                _currentInput.value =  ("$it$power")
            } else {
                onChangeCalculatorState(CalculatorState(isInvalidFormat = true))
            }
        }
    }


    fun calculateCurrentInput() {
        _currentInput.value?.let {
            val afterArithmeticOperation = it.substringAfterLast(arithmeticOperation)

            viewModelScope.launch(Dispatchers.IO) {

                val input = it.replace('×', '*')
                    .replace('÷', '/')
                val expression = Expression(input)
                val output = trimTrailingZero(java.lang.String.valueOf(expression.calculate()))
                if (output == "NaN")
                    _result.value = ""
                else _result.value = output

            }

            if (afterArithmeticOperation.isDigitsOnly())
                isDecimalPointClicked = true

            if (afterArithmeticOperation.isEmpty()) {
                isDecimalPointClicked = true

            }
        }
    }


    fun removeCharsCurrentInput() {
        _currentInput.value?.let {
            if (it != "") {
                _currentInput.value  = it.substring(0, it.length - 1)

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
            if (isDecimalPointClicked && (afterArithmeticOperation.lastOrNull()?.isDigit() == true) && afterArithmeticOperation.none { it =='.' })  {
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
            if (it.lastOrNull() in listNumbers)
                _currentInput.value = ("$it×(-")
            else _currentInput.value = ("$it(-")
        }
    }

    fun addBrackets() {
        _currentInput.value?.let {
            _currentInput.value = with(it) {
                when {
                    isBalancedBrackets(this) && lastOrNull() in listNumbers -> ("$this×(")
                    isBalancedBrackets(this) -> ("$this(")
                    else -> ("$this)")
                }
           }
        }
    }


    fun onInsert() {
        safeLet(_currentInput.value, _result.value) { currentInputValue, resultValue ->
            if (resultValue.isNotEmpty()) {
                val prevOperation = PrevOperation(currentInputValue, resultValue)
                insert(prevOperation)
                _currentInput.value = resultValue
                _result.value = ""
            }
        }
    }


    fun addPercentage() {
        _currentInput.value?.let {
            val afterArithmeticOperation = it.substringAfterLast(arithmeticOperation)
            if (afterArithmeticOperation.isNotEmpty() && (afterArithmeticOperation.lastOrNull() in listNumbers) && (afterArithmeticOperation.lastOrNull()!= '%')) {
                _currentInput.value = ("$it%")
            }
            else {
                onChangeCalculatorState(CalculatorState(isInvalidFormat = true))
            }
        }
    }

    fun addNumber(number: String) {
        _currentInput.value?.let {
            val afterArithmeticOperation = it.substringAfterLast(arithmeticOperation)
            with(afterArithmeticOperation) {
                when {
                    equals("0") ->  _currentInput.value = (it.dropLast(1) + number)
                    lastOrNull() in listChars  -> _currentInput.value =  ("$it×$number")
                    (lastOrNull() != '%') -> _currentInput.value =  (it + number)
                    else  -> onChangeCalculatorState(CalculatorState(isInvalidFormat = true))
                }
        }
         }
     }

    fun addArithmeticOperation(arithmeticSymbol: String) {
        _currentInput.value?.let {
            arithmeticOperation = arithmeticSymbol.single()
            if (it.lastOrNull() !in listArithmeticSymbols && it.isNotEmpty()) {
                _currentInput.value = (it + arithmeticSymbol)
                isDecimalPointClicked = true
            }
            else if (it.lastOrNull() in listArithmeticSymbols) {
                _currentInput.value = (it.dropLast(1) + arithmeticSymbol)
                isDecimalPointClicked = true
            }
        }
    }

    fun clear()  = viewModelScope.launch(Dispatchers.IO) {
        prevOperationRepository.clear()
    }

    private fun insert(pervOperation: PrevOperation) = viewModelScope.launch(Dispatchers.IO) {
        prevOperationRepository.insert(pervOperation)
    }

}







package com.example.calculator0.ui.calculator




import android.app.Application
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.*
import com.example.calculator0.repository.PrevOperationRepository
import com.example.calculator0.database.PrevOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.mariuszgromada.math.mxparser.Expression

class CalculatorViewModel(
    repository: PrevOperationRepository,
    application: Application
) : ViewModel() {

    private val repository1 = repository
    val allPrevOperations = repository.allPrevOperations
    val symbols = "+-÷×"
    private val symbols1: List<Char> = listOf(')','e', 'i')
    private val symbols2: List<Char> = listOf('+','-','×','÷','.')
    private val symbols5: List<Char> = listOf(')','1','2','3','4','5','6','7','8','9','0','%','e','i')
    private var isDotClicked = true
    private var action2 = '+'

    private val _currentInput: MutableStateFlow<String?> = MutableStateFlow(null)
    val currentInput = _currentInput.asStateFlow()

    private val _currentOutput: MutableStateFlow<String?> = MutableStateFlow(null)
    val currentOutput = _currentOutput.asStateFlow()

    private val _invalid = MutableSharedFlow<String>()
    val invalid = _invalid.asSharedFlow()

    private fun onInvalid() {
        viewModelScope.launch {
            _invalid.emit("Invalid format used.")
        }
    }

    init {
        _currentInput.value = ""
        _currentOutput.value = ""
    }

    //-----------------------
    // Calculator functions
    //-----------------------


    fun addToCurrentInput(number: String, currentValue: String) {
        val givenNumber = currentValue.substringAfterLast(action2)
        with(givenNumber) {
            when {
                equals("0") ->  _currentInput.value = (currentValue.dropLast(1) + number)
                lastOrNull() in symbols1 && !givenNumber.contains('.')  -> _currentInput.value =  ("$currentValue×$number")
                (lastOrNull() != '%') && !givenNumber.contains('.') -> _currentInput.value =  (currentValue +  number)
                else  -> onInvalid()
            }
        }
    }

    fun updateCurrentValue(keyWord: String, currentValue: String) {
        _currentInput.value = with(currentValue) {
            when {
                lastOrNull() in symbols5 -> ("$this×$keyWord")
                else -> ("$this$keyWord")
            }
        }
    }

    fun exponentiation(input: String, currentValue: String) {
        val givenNumber = currentValue.substringAfterLast(action2)
            if (givenNumber.isNotEmpty() && (givenNumber.lastOrNull() in symbols5)) {
                _currentInput.value =  ("$currentValue$input")
            } else {
                onInvalid()
            }
        }


    fun getResult(currentValue: String) {

        val givenNumber = currentValue.substringAfterLast(action2)

        viewModelScope.launch(Dispatchers.IO) {
            val value = currentValue.replace('×', '*')
                                    .replace('÷', '/')
            val exp = Expression(value)
            val output = trimTrailingZero(java.lang.String.valueOf(exp.calculate()))
            if (output == "NaN")
                _currentOutput.value = ""
            else _currentOutput.value = output
        }

        if (givenNumber.isDigitsOnly())
            isDotClicked = true

        if (currentValue.isEmpty()) {
            isDotClicked = true

        }
    }


    fun backspace(currentValue: String) {
        if (currentValue != "") {
            val value  = currentValue.substring(0, currentValue.length - 1)
            _currentInput.value = value
        }
    }

    fun period(currentValue: String) {
        val givenNumber = currentValue.substringAfterLast(action2)
        if ((givenNumber.isEmpty()) and isDotClicked) {
            _currentInput.value = (currentValue + "0.")
            isDotClicked = false
        }
        if (isDotClicked && (givenNumber.lastOrNull()?.isDigit() == true) && givenNumber.none { it =='.' })  {
            _currentInput.value = ("$currentValue.")
            isDotClicked = false
        }
    }

    fun clear() {
        _currentInput.value = ""
        _currentOutput.value = ""
    }

    fun sign(currentValue: String) {
        if (currentValue.lastOrNull() in symbols5)
        _currentInput.value = ("$currentValue×(-")
        else _currentInput.value = ("$currentValue(-")

    }

    fun betweenBrackets(currentValue: String) {
        _currentInput.value = with(currentValue) {
            when {
                isBalanced(this) && lastOrNull() in symbols5 -> ("$this×(")
                isBalanced(this) -> ("$this(")
                else -> ("$this)")
            }
        }
    }

    fun equals(currentValue: String, resultValue: String) {
        if (resultValue.isNotEmpty()) {

            val test = PrevOperation(currentValue, resultValue)

            insert(test)
            _currentInput.value = resultValue
            _currentOutput.value = ""
        }
    }

    fun percent(currentValue: String) {
        val givenNumber = currentValue.substringAfterLast(action2)
        if (givenNumber.isNotEmpty() && (givenNumber.lastOrNull() in symbols5) && (givenNumber.lastOrNull()!= '%')) {
            _currentInput.value = ("$currentValue%")
        }
        else {

            onInvalid()
        }
    }

    fun onNumberClickedButton(number: String, currentValue: String) {
        val givenNumber = currentValue.substringAfterLast(action2)
           with(givenNumber) {
             when {
                 equals("0") ->  _currentInput.value = (currentValue.dropLast(1) + number)
                 lastOrNull() in symbols1  -> _currentInput.value =  ("$currentValue×$number")
                 (lastOrNull() != '%') -> _currentInput.value =  (currentValue + number)
                 else  -> onInvalid()
             }
         }
     }

    fun onOperationClickedButton(action: String, currentValue: String) {
        action2 = action.single()
        if (currentValue.lastOrNull() !in symbols2 && currentValue.isNotEmpty()) {
            _currentInput.value = (currentValue + action)
            isDotClicked = true
        }
        else if (currentValue.lastOrNull() in symbols2) {
            _currentInput.value = (currentValue.dropLast(1) + action)
            isDotClicked = true
        }
    }

     private fun isBalanced(str: String): Boolean {
        var count = 0
        var i = 0
        while (i < str.length && count >= 0) {
            if (str[i] == '(') count++ else if (str[i] == ')') count--
            i++
        }
        return count == 0
    }

    private fun trimTrailingZero(value: String?): String? {
        return if (!value.isNullOrEmpty()) {
            if (value.indexOf(".") < 0) {
                value

            } else {
                value.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
            }

        } else {
            value
        }
    }

    //------------------
    // Data
    //------------------

    fun onClear() {
        viewModelScope.launch {
            deleteAll()
        }
    }

    private suspend fun deleteAll() {
        repository1.clear()
    }


    private fun insert(pervOperation: PrevOperation) = viewModelScope.launch(Dispatchers.IO) {
        repository1.insert(pervOperation)
    }

}







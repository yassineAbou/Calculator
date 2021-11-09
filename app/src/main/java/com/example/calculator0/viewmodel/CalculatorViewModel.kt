package com.example.calculator0.viewmodel




import android.app.Application
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.*
import com.example.calculator0.repository.PrevOperationRepository
import com.example.calculator0.database.PrevOperation
import kotlinx.coroutines.Dispatchers
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

    val clearHistoryEnabled: LiveData<Boolean?> = Transformations.map(allPrevOperations) {
        it?.isNotEmpty()
    }

    private val _currentInput = MutableLiveData<String> ()
    val currentInput: LiveData<String>
    get() = _currentInput

    private val _currentOutput = MutableLiveData<String> ()
    val currentOutput: LiveData<String>
        get() = _currentOutput

    private val _invalid = MutableLiveData<Boolean> ()
    val invalid: LiveData<Boolean>
        get() = _invalid

    private fun onInvalid() {
        _invalid.value = true
    }

    fun onInvalidComplete() {
        _invalid.value = false
    }

    private val _eventCalculatorFinished = MutableLiveData<Boolean> ()
    val eventCalculatorFinished: LiveData<Boolean>
        get() = _eventCalculatorFinished

    fun onCalculatorFinish() {
        _eventCalculatorFinished.value = true
    }

    fun onCalculatorFinishComplete() {
        _eventCalculatorFinished.value = false
    }

    init {
        _currentInput.value = ""
        _currentOutput.value = ""
    }

    //-----------------------
    // Calculator functions
    //-----------------------


    fun addToCurrentInput(number: String) {
        val currentValue = _currentInput.value!!
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

    fun updateCurrentValue(keyWord: String) {
        val currentValue = _currentInput.value!!
        _currentInput.value = with(currentValue) {
            when {
                lastOrNull() in symbols5 -> ("$currentValue×$keyWord")
                else -> ("$currentValue$keyWord")
            }
        }
    }

    fun exponentiation(input: String) {
        val currentValue = _currentInput.value!!
        val givenNumber = currentValue.substringAfterLast(action2)
            if (givenNumber.isNotEmpty() and (givenNumber.lastOrNull() in symbols5)) {
                _currentInput.value =  ("$currentValue$input")
            } else {
                onInvalid()
            }
        }


    fun getResult() {

        var currentValue = _currentInput.value!!
        val givenNumber = currentValue.substringAfterLast(action2)

        viewModelScope.launch(Dispatchers.Default) {
                currentValue = currentValue.replace('×', '*')
                currentValue = currentValue.replace('÷', '/')
                val exp = Expression(currentValue)
                val output = trimTrailingZero(java.lang.String.valueOf(exp.calculate()))
                if (output == "NaN")
                    _currentOutput.postValue("")
                else _currentOutput.postValue(output!!)
            }

            if (givenNumber.isDigitsOnly())
            isDotClicked = true

           if (currentValue.isEmpty()) {
               isDotClicked = true

           }
        }

    fun backspace() {
        var currentValue = _currentInput.value!!
        if (currentValue != "") {
            currentValue = currentValue.substring(0, currentValue.length - 1)
            _currentInput.value = currentValue
        }
    }

    fun period() {
        val currentValue = _currentInput.value!!
        val givenNumber = currentValue.substringAfterLast(action2)
        if ((givenNumber.isEmpty()) and isDotClicked) {
            _currentInput.value = (currentValue + "0.")
            isDotClicked = false
        }
        if (isDotClicked and (givenNumber.lastOrNull()?.isDigit() == true) and givenNumber.none { it =='.' })  {
            _currentInput.value = ("$currentValue.")
            isDotClicked = false
        }
    }

    fun clear() {
        _currentInput.value = ""
        _currentOutput.value = ""
    }

    fun sign() {
        val currentValue = _currentInput.value!!
        if (currentValue.lastOrNull() in symbols5)
        _currentInput.value = ("$currentValue×(-")
        else _currentInput.value = ("$currentValue(-")

    }

    fun betweenBrackets() {
        val currentValue = _currentInput.value!!
        _currentInput.value = with(currentValue) {
            when {
                isBalanced(currentValue) && lastOrNull() in symbols5 -> ("$currentValue×(")
                isBalanced(currentValue) -> ("$currentValue(")
                else -> ("$currentValue)")
            }
        }
    }

    fun equals() {
        val resultValue = _currentOutput.value!!
        val currentValue = _currentInput.value!!
        if (resultValue.isNotEmpty()) {

            val test = PrevOperation(currentValue, resultValue)

            insert(test)
            _currentInput.value = resultValue
            _currentOutput.value = ""
        }
    }

    fun percent() {
        val currentValue = _currentInput.value!!
        val givenNumber = currentValue.substringAfterLast(action2)
        if (givenNumber.isNotEmpty() and (givenNumber.lastOrNull() in symbols5) and (givenNumber.lastOrNull()!= '%')) {
            _currentInput.value = ("$currentValue%")
        }
        else {

            onInvalid()
        }
    }

    fun onNumberClickedButton(number: String) {
        val currentValue = _currentInput.value!!
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

    fun onOperationClickedButton(action: String) {
        val currentValue = _currentInput.value!!
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




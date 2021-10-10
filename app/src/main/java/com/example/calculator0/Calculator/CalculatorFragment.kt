package com.example.calculator0.Calculator

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.calculator0.R
import com.example.calculator0.databinding.FragmentCalculatorBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mariuszgromada.math.mxparser.Expression


class CalculatorFragment : Fragment() {

    private lateinit var binding: FragmentCalculatorBinding
    private val addition = "+"
    private val subtraction = "-"
    private val multiplication = "×"
    private val division = "÷"
    val symbols = "+-÷×"
    private val symbols1: List<Char> = listOf(')','e', 'i')
    private val symbols2: List<Char> = listOf('+','-','×','÷','.')
    private val symbols5: List<Char> = listOf(')','1','2','3','4','5','6','7','8','9','0','%','e','i')
    var isDotClicked = true
    private var action2 = '+'


    @SuppressLint("SourceLockedOrientationActivity", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        requireActivity().window.statusBarColor = Color.WHITE

        binding.apply {

            calculator = this@CalculatorFragment

            zero.setOnClickListener { onNumberClickedButton(getString(R.string._0)) }
            one.setOnClickListener { onNumberClickedButton(getString(R.string._1)) }
            two.setOnClickListener { onNumberClickedButton(getString(R.string._2)) }
            three.setOnClickListener { onNumberClickedButton(getString(R.string._3)) }
            four.setOnClickListener { onNumberClickedButton(getString(R.string._4)) }
            five.setOnClickListener { onNumberClickedButton(getString(R.string._5)) }
            six.setOnClickListener { onNumberClickedButton(getString(R.string._6))}
            seven.setOnClickListener { onNumberClickedButton(getString(R.string._7))}
            eight.setOnClickListener { onNumberClickedButton(getString(R.string._8))}
            nine.setOnClickListener { onNumberClickedButton(getString(R.string._9))}
            plus.setOnClickListener { onOperationClickedButton(addition) }
            minus.setOnClickListener { onOperationClickedButton(subtraction) }
            multiplicand.setOnClickListener { onOperationClickedButton(multiplication) }
            divided.setOnClickListener { onOperationClickedButton(division) }

            history.setOnClickListener {

                    if (group4.visibility == View.GONE) {
                      history.setImageResource(R.drawable.ic_baseline_calculate_24)
                      group3.visibility = View.INVISIBLE
                      group4.visibility = View.VISIBLE
                    } else if (group3.visibility == View.INVISIBLE) {
                        history.setImageResource(R.drawable.no_history)
                        group3.visibility = View.VISIBLE
                        group4.visibility = View.GONE
                    }

            }

            emi.setOnClickListener {
               it.findNavController()
                   .navigate(com.example.calculator0.CalculatorFragmentDirections.actionCalculatorFragmentToEmiOneFragment())
            }

            simple?.setOnClickListener {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            scientific?.setOnClickListener {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            switchArrow?.setOnClickListener {
                if (group1?.isVisible == true) {
                    group1.visibility = View.GONE
                    group2?.visibility = View.VISIBLE

                }
                else if (group2?.isVisible == true) {
                    group2.visibility = View.GONE
                    group1?.visibility = View.VISIBLE

                }
            }

            percent.setOnClickListener {
                val currentValue = currentInput.text.toString()
                val givenNumber = currentValue.substringAfterLast(action2)
                if (givenNumber.isNotEmpty() and (givenNumber.lastOrNull() in symbols5) and (givenNumber.lastOrNull()!= '%')) {
                    currentInput.text= (currentValue + getString(R.string._percent))
                }
                else {
                    Toast.makeText(activity,getString(R.string._invalid), Toast.LENGTH_SHORT).show()
                }
            }

            equals.setOnClickListener {
                val resultValue = result.text.toString()
                if (resultValue.isNotEmpty()) {
                    currentInput.text = resultValue
                    result.text = ""
                }
            }

            currentInput.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    var currentValue = currentInput.text.toString()
                    val givenNumber = currentValue.substringAfterLast(action2)

                    GlobalScope.launch(Dispatchers.Default) {
                        currentValue = currentValue.replace('×','*')
                        currentValue = currentValue.replace('÷','/')
                        val exp = Expression(currentValue)
                        val output = java.lang.String.valueOf(exp.calculate())
                        withContext(Dispatchers.Main) {
                            result.text = output
                            if (result.text.toString() == "NaN" )
                                result.text = ""
                        }

                    }

                    if (currentValue.isEmpty()) {
                        isDotClicked = true
                        noBackspace.setImageResource(R.drawable.ic_no_backspace)
                    }
                    else if (currentValue.isNotEmpty()) {
                        noBackspace.setImageResource(R.drawable.backspace)
                    }

                    if (givenNumber.isDigitsOnly()) {
                        isDotClicked = true
                    }

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
            })

            betweenBrackets.setOnClickListener {
                val currentValue = currentInput.text.toString()

                currentInput.text = with(currentValue) {
                    when {
                        isBalanced(currentValue) && lastOrNull() in symbols5 -> ("$currentValue×(")
                        isBalanced(currentValue) -> ("$currentValue(")
                        else -> ("$currentValue)")
                    }
                }.toString()

            }
            sign.setOnClickListener {
                val currentValue = binding.currentInput.text.toString()
                binding.currentInput.text = ("$currentValue(-")
            }

            clear.setOnClickListener {
                currentInput.text = ""
                result.text = ""
            }

            period.setOnClickListener {

                val currentValue = binding.currentInput.text.toString()
                val givenNumber = currentValue.substringAfterLast(action2)
                if ((givenNumber.isEmpty()) and isDotClicked) {
                    binding.currentInput.text = (currentValue + "0.")
                    isDotClicked = false
                }
                if (isDotClicked and (givenNumber.lastOrNull()?.isDigit() == true) and givenNumber.none { it =='.' })  {
                    binding.currentInput.text = ("$currentValue.")
                    isDotClicked = false

                }
            }

            noBackspace.setOnClickListener {
                var currentValue = binding.currentInput.text.toString()

                if (currentValue != "") {
                    currentValue = currentValue.substring(0, currentValue.length - 1)
                    binding.currentInput.text = currentValue
                }

            }
        }
        return binding.root
    }

    fun exponentiation(view: View) {

        fun updateCurrentInput(input: String) {
            val currentValue = binding.currentInput.text.toString()
            val givenNumber = currentValue.substringAfterLast(action2)

            if (givenNumber.isNotEmpty() and (givenNumber.lastOrNull() in symbols5)) {
                binding.currentInput.text =  ("$currentValue$input")

            } else {
                Toast.makeText(activity,
                    getString(R.string._invalid), Toast.LENGTH_SHORT).show()
            }
        }

        when (view.id) {
            R.id.squared -> updateCurrentInput("^(2)")
            R.id.power_3 -> updateCurrentInput("^(3)")
            R.id.power -> updateCurrentInput("^(")
            R.id.factorial -> updateCurrentInput("!")
            else -> return
        }
    }

      fun function(view: View) {

        fun updateCurrentValue(keyWord: String): CharSequence {

            val currentValue = binding.currentInput.text.toString()

            val lastValue = with(currentValue) {
                when {
                    lastOrNull() in symbols5 -> ("$currentValue×$keyWord")
                    else -> ("$currentValue$keyWord")
                }
            }.toString()

            return lastValue
        }
        binding.currentInput.text =  when (view.id) {

            R.id.squareRoot -> updateCurrentValue("sqrt(")
            R.id.cos -> updateCurrentValue("cos(")
            R.id.sin -> updateCurrentValue("sin(")
            R.id.tan -> updateCurrentValue("tan(")
            R.id.ln -> updateCurrentValue("ln(")
            R.id.log -> updateCurrentValue("log10(")
            R.id.one_division_slash -> updateCurrentValue("1÷")
            R.id.eˣ -> updateCurrentValue("exp(")
            R.id.verticalBars -> updateCurrentValue("abs(")
            R.id.EConstant -> updateCurrentValue("e")
            R.id.piConstant -> updateCurrentValue("pi")
            R.id.sin_minus -> updateCurrentValue("asin(")
            R.id.cos_minus -> updateCurrentValue("acos(")
            R.id.tan_minus -> updateCurrentValue("atan(")
            R.id.sinh -> updateCurrentValue("sinh(")
            R.id.cosh -> updateCurrentValue("cosh(")
            R.id.tanh -> updateCurrentValue("tanh(")
            R.id.sinh_minus -> updateCurrentValue("asinh(")
            R.id.cosh_minus -> updateCurrentValue("acosh(")
            R.id.tanh_minus -> updateCurrentValue("atanh(")
            R.id.two_Power -> updateCurrentValue("2^(")
            R.id.cureRoot -> updateCurrentValue("root(3,")
            R.id.Rad -> updateCurrentValue("rad(")
            R.id.Deg -> updateCurrentValue("deg(")
            else -> return
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

    private fun onNumberClickedButton(number: String) {
        val currentValue = binding.currentInput.text.toString()
        val givenNumber = currentValue.substringAfterLast(action2)
        binding.currentInput.text =  with(givenNumber) {
            when {
                equals("0") -> (currentValue.dropLast(1) + number)
                lastOrNull() in symbols1  -> ("$currentValue×$number")
                (lastOrNull() != '%') -> (currentValue +  number)
                else  -> return
            }
        }.toString()

    }

    private fun onOperationClickedButton(action: String) {
        action2 = action.single()
        val currentValue = binding.currentInput.text.toString()

        if (currentValue.lastOrNull() !in symbols2 && currentValue.isNotEmpty()) {
            binding.currentInput.text= (currentValue + action)
            isDotClicked = true
        }
        else if (currentValue.lastOrNull() in symbols2) {
            binding.currentInput.text= (currentValue.dropLast(1) + action)
            isDotClicked = true

        }

    }

}
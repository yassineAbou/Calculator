package com.example.calculator0.calculator

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.calculator0.R
import com.example.calculator0.databinding.FragmentCalculatorBinding


import kotlinx.coroutines.*
import org.mariuszgromada.math.mxparser.Expression


class CalculatorFragment : Fragment() {


    private lateinit var binding: FragmentCalculatorBinding
    private val viewModel: CalculatorViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        requireActivity().window.statusBarColor = Color.WHITE

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner



        binding.currentInput.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    viewModel.getResult()
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) { }
            })

        binding.apply {
            switchArrow?.setOnClickListener {
                if (group1?.isVisible == true) {
                    group1.visibility = View.GONE
                    group2?.visibility = View.VISIBLE
                }
                else if (binding.group2?.isVisible == true) {
                    group2?.visibility = View.GONE
                    group1?.visibility = View.VISIBLE

                }
            }

            simple?.setOnClickListener {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            scientific?.setOnClickListener {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            history.setOnClickListener {

                if (group4.visibility == View.GONE) {
                    history.setImageResource(R.drawable.ic_baseline_calculate_24)
                    group3.visibility = View.INVISIBLE
                    group4.visibility = View.VISIBLE
                    if (group2?.visibility == View.VISIBLE) {
                        group2.visibility = View.INVISIBLE
                    } else if (group1?.visibility == View.VISIBLE) {
                        group1.visibility = View.INVISIBLE
                    }


                } else if (group4.visibility == View.VISIBLE) {
                    history.setImageResource(R.drawable.no_history)
                    group3.visibility = View.VISIBLE
                    group4.visibility = View.GONE
                    if (group2?.visibility == View.INVISIBLE) {
                        group2.visibility = View.VISIBLE
                    } else if (group1?.visibility == View.INVISIBLE) {
                        group1.visibility = View.VISIBLE
                    }
                }

            }
        }

            viewModel.invalid.observe(viewLifecycleOwner, Observer { isInvalid ->
                if (isInvalid)
                    toast()
            })

            viewModel.eventCalculatorFinished.observe(viewLifecycleOwner, Observer { hasFinished ->
                if (hasFinished)
                    calculatorFinished()
            })



        return binding.root
    }

    private fun calculatorFinished() {
        val action = CalculatorFragmentDirections.actionCalculatorFragmentToEmiOneFragment()
        findNavController(this).navigate(action)
        viewModel.onCalculatorFinishComplete()
    }

    private fun toast() {
        Toast.makeText(activity,
            getString(R.string._invalid), Toast.LENGTH_SHORT).show()
        viewModel.onInvalidComplete()
    }

}
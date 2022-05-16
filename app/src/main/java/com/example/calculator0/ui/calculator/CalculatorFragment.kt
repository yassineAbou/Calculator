package com.example.calculator0.ui.calculator

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator0.R
import com.example.calculator0.databinding.FragmentCalculatorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalculatorFragment : Fragment() {

    private val calculatorViewModel: CalculatorViewModel by viewModels()
    private lateinit var binding: FragmentCalculatorBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        binding.viewModel = calculatorViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = PrevOperationAdapter(PrevOperationListener { number ->
            calculatorViewModel.currentInput.value?.let { calculatorViewModel.addToCurrentInput(number, it) }
        })

        binding.recyclerview.adapter = adapter
        val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        binding.recyclerview.layoutManager = manager



        binding.apply {

            simple?.setOnClickListener {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            scientific?.setOnClickListener {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            switchArrow?.setOnClickListener {
                onChangeScientificButtonsVisibility()
            }

            history.setOnClickListener {
                if (!calculatorViewModel.calculatorState.value.showScientificButtons1 && !calculatorViewModel.calculatorState.value.showScientificButtons2) {
                    calculatorViewModel.onChangeCalculatorState(CalculatorState(showScientificButtons1 = true))
                }
                showAndHideHistory()
                binding.recyclerview.scrollToPosition(0)
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            calculatorViewModel.currentInput.collectLatest {
                it?.let {
                    if (it.isNotEmpty()) {
                        binding.backspace.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.purple),
                            PorterDuff.Mode.SRC_ATOP)
                    } else {
                        binding.backspace.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.purple_light),
                            PorterDuff.Mode.SRC_ATOP)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calculatorViewModel.calculatorState.collect { calculatorState ->
                    when {
                        calculatorState.invalidFormat -> invalidFormat()
                        calculatorState.showScientificButtons2 -> showScientificButtons2()
                        calculatorState.showScientificButtons1 -> showScientificButtons1()
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            calculatorViewModel.allPrevOperations.collectLatest { allPrevOperations ->
                adapter.submitList(allPrevOperations)

                when (allPrevOperations.size) {
                    0 -> {
                        disableHistoryButton()
                        hideHistoryGroup()
                    }
                    else -> enableHistoryButton()
                }

            }
        }



        binding.emi.setOnClickListener {
            calculatorFinished()
        }

        return binding.root
    }

    private fun onChangeScientificButtonsVisibility() {
        if (binding.scientificButtonsGroup1?.isVisible == true) {
            onChangeScientificButtons2State()
        } else if (binding.scientificButtonsGroup2?.isVisible == true) {
            onChangeScientificButtons1State()
        }
    }

    private fun onChangeScientificButtons1State() {
        calculatorViewModel.onChangeCalculatorState(CalculatorState(showScientificButtons2 = false))
        calculatorViewModel.onChangeCalculatorState(CalculatorState(showScientificButtons1 = true))
    }

    private fun onChangeScientificButtons2State() {
        calculatorViewModel.onChangeCalculatorState(CalculatorState(showScientificButtons1 = false))
        calculatorViewModel.onChangeCalculatorState(CalculatorState(showScientificButtons2 = true))
    }

    private fun enableHistoryButton() {
        binding.history.isEnabled = true
        binding.history.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.light_black),
            PorterDuff.Mode.SRC_ATOP)
    }

    private fun disableHistoryButton() {
        binding.history.isEnabled = false
        binding.history.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.gray_light),
            PorterDuff.Mode.SRC_ATOP)
    }


    private fun invalidFormat() {
        Toast.makeText(requireContext(), "Invalid format used.", Toast.LENGTH_SHORT).show()
        calculatorViewModel.onChangeCalculatorState(CalculatorState(invalidFormat = false))
    }

    private fun showScientificButtons1() {
        binding.scientificButtonsGroup2?.visibility = GONE
        binding.scientificButtonsGroup1?.visibility = VISIBLE
    }

    private fun showScientificButtons2() {
        binding.scientificButtonsGroup1?.visibility  = INVISIBLE
        binding.scientificButtonsGroup2?.visibility = VISIBLE
    }

    private fun showAndHideHistory() {
        binding.apply {
            if (historyGroup.isVisible) {
                hideHistoryGroup()
            } else {
                showHistoryGroup()
            }
        }
    }

    private fun hideHistoryGroup() {
        binding.apply {
            history.setImageResource(R.drawable.history)
            historyGroup.visibility = GONE
            simpleButtonsGroup.visibility =  VISIBLE

            if (calculatorViewModel.calculatorState.value.showScientificButtons1) {
                scientificButtonsGroup1?.visibility = VISIBLE
            } else if (calculatorViewModel.calculatorState.value.showScientificButtons2) {
                scientificButtonsGroup2?.visibility = VISIBLE
            }
        }
    }

    private fun showHistoryGroup() {
        binding.apply {
            history.setImageResource(R.drawable.ic_baseline_calculate_24)
            historyGroup.visibility = VISIBLE
            simpleButtonsGroup.visibility =  INVISIBLE

            if (scientificButtonsGroup1?.isVisible == true) {
                scientificButtonsGroup1.visibility = INVISIBLE
            } else if (scientificButtonsGroup2?.isVisible == true) {
                scientificButtonsGroup2.visibility = INVISIBLE

            }
        }
    }


    private fun calculatorFinished() {
        val action =
            CalculatorFragmentDirections.actionCalculatorFragmentToEmiOneFragment()
        findNavController(this).navigate(action)
    }



}
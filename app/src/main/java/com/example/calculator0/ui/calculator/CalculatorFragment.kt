package com.example.calculator0.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator0.R
import com.example.calculator0.database.PrevOperationDatabase
import com.example.calculator0.databinding.FragmentCalculatorBinding
import com.example.calculator0.repository.PrevOperationRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CalculatorFragment : Fragment() {

    lateinit var calculatorViewModel: CalculatorViewModel
    private lateinit var binding: FragmentCalculatorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)


        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dao = PrevOperationDatabase.getInstance(application).prevOperationDoe
        val repository = PrevOperationRepository(dao)
        val viewModelFactory = CalculatorViewModelFactory(repository, application)

        // Get a reference to the ViewModel associated with this fragment.

         calculatorViewModel =
             ViewModelProvider(
                 this, viewModelFactory)[CalculatorViewModel::class.java]



        binding.viewModel = calculatorViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = PrevOperationAdapter(PrevOperationListener { number ->
            calculatorViewModel.currentInput.value?.let { calculatorViewModel.addToCurrentInput(number, it) }
        })

        binding.recyclerview.adapter = adapter
        val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        binding.recyclerview.layoutManager = manager


        binding.apply {

            switchArrow?.setOnClickListener {
                if (group1?.isVisible == true) {
                    showScientificButtons1()
                }
                else if (binding.group2?.isVisible == true) {
                    showScientificButtons2()
                }
            }

            history.setOnClickListener {
                showAndHideHistory()
            }
        }


        lifecycle.coroutineScope.launch {
            calculatorViewModel.allPrevOperations.collectLatest {
                adapter.submitList(it)
            }
        }


        lifecycleScope.launchWhenStarted {
            calculatorViewModel.invalid.collectLatest {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()

            }
        }

        binding.emi.setOnClickListener {
            calculatorFinished()
        }

        return binding.root
    }


    private fun showScientificButtons2() {
        binding.group2?.visibility = View.GONE
        binding.group1?.visibility = View.VISIBLE
    }

    private fun showScientificButtons1() {
        binding.group1?.visibility  = View.GONE
        binding.group2?.visibility = View.VISIBLE
    }

    private fun showAndHideHistory() {
        binding.apply {
            when {
                group4.visibility == View.GONE -> {
                    showGroup4()
                    if (group2?.visibility == View.VISIBLE) {
                        group2.visibility = View.INVISIBLE
                    } else if (group1?.visibility == View.VISIBLE) {
                        group1.visibility = View.INVISIBLE
                    }
                }
                group4.visibility == View.VISIBLE -> {
                    hideGroup4()
                    if (group2?.visibility == View.INVISIBLE) {
                        group2.visibility = View.VISIBLE
                    } else if (group1?.visibility == View.INVISIBLE) {
                        group1.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    private fun hideGroup4() {
        binding.apply {
            history.setImageResource(R.drawable.history)
            group3.visibility = View.VISIBLE
            group4.visibility = View.GONE
        }
    }

    private fun showGroup4() {
        binding.apply {
            history.setImageResource(R.drawable.ic_baseline_calculate_24)
            group3.visibility = View.INVISIBLE
            group4.visibility = View.VISIBLE
        }
    }


    private fun calculatorFinished() {
        val action =
            CalculatorFragmentDirections.actionCalculatorFragmentToEmiOneFragment()
        findNavController(this).navigate(action)
    }

}
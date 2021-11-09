package com.example.calculator0.ui.calculator

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator0.R
import com.example.calculator0.database.PrevOperationDatabase
import com.example.calculator0.databinding.FragmentCalculatorBinding
import com.example.calculator0.repository.PrevOperationRepository
import com.example.calculator0.viewmodel.CalculatorViewModel
import com.example.calculator0.viewmodel.CalculatorViewModelFactory


class CalculatorFragment : Fragment() {

    lateinit var viewModel: CalculatorViewModel
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
         viewModel =
             ViewModelProvider(
                 this, viewModelFactory)[CalculatorViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = PrevOperationAdapter(PrevOperationListener { number ->
            viewModel.addToCurrentInput(number)
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


            viewModel.allPrevOperations.observe(viewLifecycleOwner, {
                it?.let {
                    adapter.submitList(it)
                }
            })

            viewModel.invalid.observe(viewLifecycleOwner, { isInvalid ->
                if (isInvalid)
                    toast()
            })

            viewModel.eventCalculatorFinished.observe(viewLifecycleOwner, { hasFinished ->
                if (hasFinished)
                    calculatorFinished()
            })


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
            if (group4.visibility == View.GONE) {
                history.setImageResource(R.drawable.ic_baseline_calculate_24)
                group3.visibility = View.INVISIBLE
                group4.visibility = View.VISIBLE
                if (group2?.visibility == View.VISIBLE) {
                    group2.visibility = View.INVISIBLE
                } else if (group1?.visibility == View.VISIBLE) {
                    group1.visibility = View.INVISIBLE
                }
            }
            else if (group4.visibility == View.VISIBLE) {
                history.setImageResource(R.drawable.history)
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

    private fun toast() {
        Toast.makeText(activity,
            getString(R.string._invalid), Toast.LENGTH_SHORT).show()
        viewModel.onInvalidComplete()
    }

    private fun calculatorFinished() {
        val action =
            CalculatorFragmentDirections.actionCalculatorFragmentToEmiOneFragment()
        findNavController(this).navigate(action)
        viewModel.onCalculatorFinishComplete()
    }

}
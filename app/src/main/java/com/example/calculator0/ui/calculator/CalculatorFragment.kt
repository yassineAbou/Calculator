package com.example.calculator0.ui.calculator

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
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator0.*
import com.example.calculator0.database.PrevOperationDatabase
import com.example.calculator0.databinding.FragmentCalculatorBinding
import com.example.calculator0.repository.PrevOperationRepository
import com.example.calculator0.viewmodel.CalculatorViewModel
import com.example.calculator0.viewmodel.CalculatorViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class CalculatorFragment : Fragment() {

    lateinit var viewModel: CalculatorViewModel
    private lateinit var binding: FragmentCalculatorBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        requireActivity().window.statusBarColor = Color.WHITE

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dao = PrevOperationDatabase.getInstance(application).prevOperationDoe
        val repository = PrevOperationRepository(dao)
        val viewModelFactory = CalculatorViewModelFactory(repository, application)

        // Get a reference to the ViewModel associated with this fragment.
         viewModel =
            ViewModelProvider(
                this, viewModelFactory).get(CalculatorViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = PrevOperationAdapter(PrevOperationListener { number ->

            viewModel.addNumber(number)
        })
        binding.recyclerview.adapter = adapter
        val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        binding.recyclerview.layoutManager = manager


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



        binding.currentInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.getResult()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) { }
        })

            viewModel.allPrevOperations.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)

                }
            })

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
        val action =
            CalculatorFragmentDirections.actionCalculatorFragmentToEmiOneFragment()
        findNavController(this).navigate(action)
        viewModel.onCalculatorFinishComplete()
    }

    private fun toast() {
        Toast.makeText(activity,
            getString(R.string._invalid), Toast.LENGTH_SHORT).show()
        viewModel.onInvalidComplete()
    }

}
package com.example.calculator0

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.calculator0.databinding.FragmentCalculatorBinding
import com.example.calculator0.databinding.FragmentEmiBinding


class EMIFragment : Fragment() {

    private lateinit var binding: FragmentEmiBinding

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmiBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.gray2)

        binding.calculate.setOnClickListener {
            it.findNavController()
                .navigate(EMIFragmentDirections.actionEmiOneFragmentToResultFragment())
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
    }

}
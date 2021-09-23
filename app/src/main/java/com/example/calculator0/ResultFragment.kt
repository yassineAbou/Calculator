package com.example.calculator0

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.calculator0.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.apply {

            add.setOnClickListener {
                it.findNavController()
                    .navigate(ResultFragmentDirections.actionResultFragmentToEmiTwoFragment())
            }

            done.setOnClickListener {
                it.findNavController()
                    .navigate(ResultFragmentDirections.actionResultFragmentToCalculatorFragment())
            }
        }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
    }


}
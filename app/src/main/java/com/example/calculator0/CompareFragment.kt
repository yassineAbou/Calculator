package com.example.calculator0

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.calculator0.databinding.FragmentCompareBinding
import com.example.calculator0.databinding.FragmentEmiBinding


class CompareFragment : Fragment() {

    private lateinit var binding: FragmentCompareBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompareBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.apply {

            done2.setOnClickListener {
                it.findNavController()
                    .navigate(CompareFragmentDirections.actionCompareFragmentToCalculatorFragment())

            }

            reset.setOnClickListener {
                it.findNavController()
                    .navigate(CompareFragmentDirections.actionCompareFragmentToEmiOneFragment())
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
    }

}
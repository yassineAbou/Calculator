package com.example.calculator0

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.calculator0.databinding.FragmentEmiTwoBinding
import com.example.calculator0.databinding.FragmentResultBinding


class ThreeFragment : Fragment() {


    private lateinit var binding: FragmentEmiTwoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEmiTwoBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.compare.setOnClickListener {
            it.findNavController()
                .navigate(ThreeFragmentDirections.actionEmiTwoFragmentToCompareFragment())
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
    }


}
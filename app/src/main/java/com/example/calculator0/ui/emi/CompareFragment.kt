package com.example.calculator0.ui.emi

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import com.example.calculator0.R
import com.example.calculator0.databinding.FragmentCompareBinding
import com.example.calculator0.utils.ifNotNull


class CompareFragment : Fragment() {



    private lateinit var binding: FragmentCompareBinding
    private val emiViewModel : EMIViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompareBinding.inflate(inflater, container, false)

        binding.viewModel = emiViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        portraitMode()

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

    private fun portraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    //-------------------
    // SHARING VALUES
    //-------------------

    private fun getShareIntent() : Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        ifNotNull(
            emiViewModel.emiFirstResult.value,
            emiViewModel.emiSecondResult.value
        ) { first, second ->


            binding.apply {
                shareIntent.setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.share_compare, first.principal,
                            first.emiMonth, first.interestRate, first.installment,
                            second.principal, second.emiMonth, second.interestRate, second.installment)
                    )
            }

        }
        return shareIntent

    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
        if(getShareIntent().resolveActivity(requireActivity().packageManager)==null){
            menu.findItem(R.id.share).isVisible = false
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }

}
package com.example.calculator0.emi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import com.example.calculator0.R
import com.example.calculator0.databinding.FragmentCompareBinding


class CompareFragment : Fragment() {



    private lateinit var binding: FragmentCompareBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompareBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        val args2 = CompareFragmentArgs.fromBundle(requireArguments())

        binding.apply {

            setFragmentResultListener("requestKey") { key, bundle ->

                firstLoan.text = bundle.getString("firstLoan")
                firstEmi.text = bundle.getString("firstEmi")
                firsInstallments.text = bundle.getString("firstInstallments")
                firstTotal.text = getString(R.string.total, bundle.getString("firstTotal"))
                firstPrincipal.text = bundle.getString("firstLoan")
                interestOptionOne.text = bundle.getString("interestOptionOne")
                firstInterest.text = bundle.getString("firstInterest")

            }
            secondLoan.text = args2.principal2
            secondPrincipal.text = args2.principal2
            secondInstallments.text = args2.installment2
            interestOptionTwo.text = args2.interset2
            secondEmi.text = args2.emiMonth2
            secondTotal.text = getString(R.string.total, args2.total2)
            secondInterest.text = args2.intersetOutput2

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

    //-------------------
    // SHARING VALUES
    //-------------------

    fun getShareIntent() : Intent {
        val args2 = CompareFragmentArgs.fromBundle(requireArguments())
        val shareIntent = Intent(Intent.ACTION_SEND)
        binding.apply {
            shareIntent.setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_compare, firstLoan.text.toString(),
                firstEmi.text.toString(), firstInterest.text.toString(), firsInstallments.text.toString(),
                args2.principal2, args2.emiMonth2, args2.intersetOutput2, args2.installment2))
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
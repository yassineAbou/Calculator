package com.example.calculator0.ui.emi


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.calculator0.R
import com.example.calculator0.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        val args = ResultFragmentArgs.fromBundle(requireArguments())

        binding.apply {

            emiAmount.text  = args.emiMonth
            interest.text = args.interest
            totalAmount.text = getString(R.string.total, args.total)
            installments.text = getString(R.string._installments, args.installment)
            principal.text = args.principal


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

    //-------------------
    // SHARING VALUES
    //-------------------

    private fun getShareIntent() : Intent {
        val args = ResultFragmentArgs.fromBundle(requireArguments())
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_result, args.principal, args.emiMonth,
                args.interestRateOutput, args.installment))
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
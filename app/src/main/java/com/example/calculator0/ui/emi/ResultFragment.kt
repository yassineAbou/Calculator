package com.example.calculator0.ui.emi


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.calculator0.R
import com.example.calculator0.databinding.FragmentResultBinding


class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val emiViewModel : EMIViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false)

        binding.viewModel = emiViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        portraitMode()

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



    //----------------------
    // Orientation
    //-----------------------

    private fun portraitMode() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    //-------------------
    // SHARING VALUES
    //-------------------


    private fun getShareIntent() : Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT,
                emiViewModel.emiFirstResult.value?.let {
                    getString(R.string.share_result, it.principal, it.emiMonth,
                        it.interestRateOutput, it.installment)
                 }
            )
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


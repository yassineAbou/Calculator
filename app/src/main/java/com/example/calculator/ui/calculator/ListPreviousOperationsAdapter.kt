package com.example.calculator.ui.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.data.PreviousOperation
import com.example.calculator.databinding.ListPreviousOperationsBinding


class ListPreviousOperationsAdapter(private val previousOperationActions: PreviousOperationActions)
  : ListAdapter<PreviousOperation, ListPreviousOperationsAdapter.ViewHolder>(PrevOperationDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prevOperation = getItem(position)!!
        holder.bind(prevOperation, previousOperationActions)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val listPreviousOperationsBinding: ListPreviousOperationsBinding)
        : RecyclerView.ViewHolder(listPreviousOperationsBinding.root) {

        fun bind(
            previousOperation: PreviousOperation,
            previousOperationActions: PreviousOperationActions,
        ) {
            listPreviousOperationsBinding.previousOperation = previousOperation
            listPreviousOperationsBinding.previousOperationActions = previousOperationActions
            listPreviousOperationsBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = com.example.calculator.databinding.ListPreviousOperationsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class PrevOperationDiffCallback : DiffUtil.ItemCallback<PreviousOperation>() {
    override fun areItemsTheSame(oldItem: PreviousOperation, newItem: PreviousOperation): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: PreviousOperation, newItem: PreviousOperation): Boolean {
        return oldItem == newItem
    }
}

interface PreviousOperationActions {
    fun addPreviousNumber(number: String)
}








package com.example.calculator.ui.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.database.PrevOperation
import com.example.calculator.databinding.ListPrevOperationsBinding


class PrevOperationAdapter(private val prevOperationActions: PrevOperationActions)
  : ListAdapter<PrevOperation, PrevOperationAdapter.ViewHolder>(PrevOperationDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prevOperation = getItem(position)!!
        holder.bind(prevOperation, prevOperationActions)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val listPrevOperationsBinding: ListPrevOperationsBinding)
        : RecyclerView.ViewHolder(listPrevOperationsBinding.root) {

        fun bind(
            prevOperation: PrevOperation,
            prevOperationActions: PrevOperationActions,
        ) {
            listPrevOperationsBinding.prevOperation = prevOperation
            listPrevOperationsBinding.prevOperationActions = prevOperationActions
            listPrevOperationsBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListPrevOperationsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class PrevOperationDiffCallback : DiffUtil.ItemCallback<PrevOperation>() {
    override fun areItemsTheSame(oldItem: PrevOperation, newItem: PrevOperation): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: PrevOperation, newItem: PrevOperation): Boolean {
        return oldItem == newItem
    }
}

class PrevOperationActions(val prevOperationActions: (number: String) -> Unit) {

    fun addPrevInput(prevOperation: PrevOperation) = prevOperationActions(prevOperation.input)

    fun addPrevResult(prevOperation: PrevOperation) = prevOperationActions(prevOperation.result)
}







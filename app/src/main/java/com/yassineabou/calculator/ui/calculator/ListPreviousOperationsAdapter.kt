package com.yassineabou.calculator.ui.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yassineabou.calculator.data.model.PreviousOperation
import com.yassineabou.calculator.databinding.PreviousOperationItemBinding

class ListPreviousOperationsAdapter(private val previousOperationAction: PreviousOperationAction) :
    ListAdapter<PreviousOperation, ListPreviousOperationsAdapter.ViewHolder>(
        PrevOperationDiffCallback(),
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prevOperation = getItem(position)
        holder.bind(prevOperation, previousOperationAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
        private val previousOperationItemBinding: PreviousOperationItemBinding,
    ) :
        RecyclerView.ViewHolder(previousOperationItemBinding.root) {

        fun bind(
            previousOperation: PreviousOperation,
            previousOperationAction: PreviousOperationAction,
        ) {
            previousOperationItemBinding.previousOperation = previousOperation
            previousOperationItemBinding.previousOperationActions = previousOperationAction
            previousOperationItemBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PreviousOperationItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class PrevOperationDiffCallback : DiffUtil.ItemCallback<PreviousOperation>() {
    override fun areItemsTheSame(oldItem: PreviousOperation, newItem: PreviousOperation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PreviousOperation,
        newItem: PreviousOperation,
    ): Boolean {
        return oldItem == newItem
    }
}

interface PreviousOperationAction {
    fun appendNumberFromHistory(number: String)
}

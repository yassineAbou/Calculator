package com.example.calculator0.ui.calculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator0.R
import com.example.calculator0.database.PrevOperation
import com.example.calculator0.databinding.ListItemPrevOperationBinding

class PrevOperationAdapter(val clickListener: PrevOperationListener)
  : ListAdapter<PrevOperation, PrevOperationAdapter.ViewHolder>(PrevOperationDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(item, clickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemPrevOperationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: PrevOperation,
            clickListener: PrevOperationListener,
        ) {
            binding.prevOperation = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPrevOperationBinding.inflate(layoutInflater, parent, false)
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

class PrevOperationListener(val clickListener: (number: String) -> Unit) {

    fun onClick(prevOperation: PrevOperation) =
      clickListener(prevOperation.previousInput)

    fun onClick2(prevOperation: PrevOperation) =
        clickListener(prevOperation.previousOutput)
}






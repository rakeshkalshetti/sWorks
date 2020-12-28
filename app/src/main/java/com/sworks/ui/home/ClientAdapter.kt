package com.sworks.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sworks.databinding.ClientLayoutBinding
import com.sworks.model.Order

class ClientAdapter(private val clickListener: View.OnClickListener) :
    ListAdapter<Order, ViewHolder>(UserItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ClientLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, clickListener)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            holder.visibleGroup()
        }
    }
}

class UserItemDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem

}


class ViewHolder(
    private val binding: ClientLayoutBinding,
    clickListener: View.OnClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.callButton.setOnClickListener(clickListener)
        binding.directionButton.setOnClickListener(clickListener)
    }

    fun bind(order: Order) {

        binding.order = order
        binding.callButton.tag = order
        binding.directionButton.tag = order
        binding.executePendingBindings()

    }

    fun visibleGroup() {
        if (binding.groupView.visibility == View.VISIBLE) {
            binding.groupView.visibility = View.GONE
        } else {
            binding.groupView.visibility = View.VISIBLE
        }
    }
}
package com.gravityloft.androidassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gravityloft.androidassignment.*
import com.gravityloft.androidassignment.`interface`.RecyclerviewInteractionListener
import com.gravityloft.androidassignment.databinding.RowPageBinding
import com.gravityloft.androidassignment.model.Item


class RecyclerViewAdapter(
    val clickListener: RecyclerviewInteractionListener
) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    var items = ArrayList<Item>()

    fun setListData(data: ArrayList<Item>) {
        this.items = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position], clickListener)

    }


    class MyViewHolder private constructor(val binding: RowPageBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: Item, clickListener: RecyclerviewInteractionListener) {


            itemView.setOnClickListener {
                clickListener.onItemSelected(position = adapterPosition, item = item)
            }
            binding.itemModel = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val binding = RowPageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MyViewHolder(binding)
            }
        }

    }

}
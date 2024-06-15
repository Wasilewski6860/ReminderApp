package com.example.reminderapp.presentation.new_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.databinding.ColorRecyclerItemBinding
import com.example.reminderapp.utils.ColorItem
import org.koin.core.component.KoinComponent

class ColorListAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<ColorItem, ColorListAdapter.ColorViewHolder>(DiffCallBack), KoinComponent {

    class ColorViewHolder(val binding: ColorRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ColorRecyclerItemBinding.inflate(inflater, parent, false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            item.color?.let {
                colorCircleItem.circleColor = it
            }
            colorCircleItem.setOnClickListener {
                onItemClickListener.onClickItem(item)
            }
        }

    }

    interface OnItemClickListener {
        fun onClickItem(colorItem: ColorItem)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<ColorItem>() {

            override fun areItemsTheSame(oldItem: ColorItem, newItem: ColorItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ColorItem, newItem: ColorItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}
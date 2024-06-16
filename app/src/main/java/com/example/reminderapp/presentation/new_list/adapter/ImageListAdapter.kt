package com.example.reminderapp.presentation.new_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.databinding.ColorRecyclerItemBinding
import com.example.reminderapp.utils.ImageItem
import org.koin.core.component.KoinComponent

class ImageListAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<ImageItem, ImageListAdapter.ImageViewHolder>(DiffCallBack), KoinComponent {

    class ImageViewHolder(val binding: ColorRecyclerItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onClickItem(imageItem: ImageItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ColorRecyclerItemBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)

        with (holder.binding) {
            item?.image?.let {
                colorCircleItem.bitmap = it
            }
            colorCircleItem.setOnClickListener {
                onItemClickListener.onClickItem(item)
            }
        }
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<ImageItem>() {

            override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem == newItem
            }

        }
    }

}
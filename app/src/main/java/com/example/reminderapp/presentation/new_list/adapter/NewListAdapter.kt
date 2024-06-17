package com.example.reminderapp.presentation.new_list.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ColorRecyclerItemBinding
import com.example.reminderapp.utils.ColorItem
import com.example.reminderapp.utils.ColorsUtils
import com.example.reminderapp.utils.ImageItem
import com.example.reminderapp.utils.ImageUtils

class NewListAdapter(
    private val listener: OnItemClickListener,
    context: Context
) : RecyclerView.Adapter<NewListAdapter.ItemHolder>() {

    private val colorsList = ColorsUtils(context).onlyColors
    private val imagesList = ImageUtils(context).onlyImages

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ColorRecyclerItemBinding.bind(view)

        fun bind(color: ColorItem, image: ImageItem) = with(binding) {
            color.color?.let { colorCircleItem.circleColor = it }
            image.image?.let { colorCircleItem.bitmap = it }

            colorCircleItem.setOnClickListener {
                listener.onItemClick(position = adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.color_recycler_item, parent, false
        )
        return ItemHolder(view)
    }

    override fun getItemCount(): Int = colorsList.size + imagesList.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(
            if (position < colorsList.size)
                colorsList[position]
            else
                ColorItem("STUB", Color.TRANSPARENT),
            if (position >= colorsList.size)
                imagesList[position - colorsList.size]
            else
                ImageItem("STUB", null)
        )
    }

}
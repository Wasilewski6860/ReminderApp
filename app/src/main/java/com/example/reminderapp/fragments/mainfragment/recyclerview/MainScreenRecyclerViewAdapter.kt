package com.example.reminderapp.fragments.mainfragment.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Task
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ReminderRecyclerViewItemBinding

class MainScreenRecyclerViewAdapter(
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<MainScreenRecyclerViewAdapter.ItemHolder>() {

    private val itemsList = mutableListOf<Task>()

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ReminderRecyclerViewItemBinding.bind(view)
        private val cardViewHolder: CardView = binding.reminderRcItemCardViewMainHolder

        init {
            // add change cardView backgroundTint color method here

            cardViewHolder.setOnClickListener {
                listener.onRcItemClick(adapterPosition)
            }
        }

        fun bind(item: Task) = with(binding) {
            reminderRcItemName.text = item.reminderName
            // reminderRcItemTime.text = 'somehow add time'
        }
    }

    interface OnItemClickListener {
        fun onRcItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.reminder_recycler_view_item, parent, false
        )
        return ItemHolder(view)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(itemsList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: Task) {
        itemsList.add(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillRecyclerWithFullItemsList(itemsMutableList: MutableList<Task>) {
        for (item in itemsMutableList) {
            itemsList.add(item)
            notifyDataSetChanged()
        }
    }

    fun getItemPosition(item: Task): Int = itemsList.indexOf(item)

    fun changeItem(position: Int, item: Task) {
        itemsList[position] = item
        notifyItemChanged(position)
    }

}
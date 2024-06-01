package com.example.reminderapp.presentation.statisticscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Task
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ReminderStatisticRecyclerviewItemBinding

class StatisticScreenRecyclerViewAdapter :
    RecyclerView.Adapter<StatisticScreenRecyclerViewAdapter.ItemHolder>() {

    private val itemsList = mutableListOf<Task>()

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ReminderStatisticRecyclerviewItemBinding.bind(view)

        fun bind(item: Task) = with(binding) {
            reminderRcItemNameTextView.text = item.name
            // reminderRcItemCounterTextView.text = ...some counter...
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.reminder_statistic_recyclerview_item, parent, false
        )
        return ItemHolder(view)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(itemsList[position])
    }

    fun fillRecyclerViewWithItems(list: List<Task>) {
        list.forEach {
            itemsList.add(it)
            notifyDataSetChanged()
        }
    }

}
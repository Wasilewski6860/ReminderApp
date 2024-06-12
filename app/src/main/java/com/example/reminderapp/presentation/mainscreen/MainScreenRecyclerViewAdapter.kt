package com.example.reminderapp.presentation.mainscreen

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ReminderRecyclerViewItemBinding
import com.example.reminderapp.presentation.creatorscreen.SpinnerPeriodicTime
import java.util.Date
import java.util.Locale

class MainScreenRecyclerViewAdapter(
    private var listener: OnItemClickListener,
    context: Context
) : RecyclerView.Adapter<MainScreenRecyclerViewAdapter.ItemHolder>() {

    private val itemsList = mutableListOf<Task>()
    private val timesDict = SpinnerPeriodicTime.getTimesDict(context)

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ReminderRecyclerViewItemBinding.bind(view)

        fun bind(item: Task) = with(binding) {
            reminderRcItemName.text = item.name
            reminderRcItemTime.text = changeTimeFormat(item) ?: "ERROR"
            reminderRcItemCardViewMainHolder.apply {
                setOnClickListener { listener.onRcItemClick(position = adapterPosition) }
                setCardBackgroundColor(item.color)
            }
        }

        private fun changeTimeFormat(task: Task): String? {
            when (task.type) {
                TaskPeriodType.ONE_TIME -> {
                    val formatDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    val date = Date(task.reminderCreationTime + task.reminderTimeTarget)
                    return formatDate.format(date)
                }
                TaskPeriodType.PERIODIC -> {
                    return timesDict[task.reminderTimeTarget]
                }
            }
        }

    }

    interface OnItemClickListener {
        fun onRcItemClick(position: Int)
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

    fun getItemByPosition(position: Int): Task {
        return itemsList[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: Task) {
        itemsList.add(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillRecyclerWithFullItemsList(itemsMutableList: List<Task>) {
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
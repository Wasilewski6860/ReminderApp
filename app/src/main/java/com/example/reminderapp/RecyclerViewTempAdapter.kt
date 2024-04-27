package com.example.reminderapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.databinding.ReminderRcItemBinding

class RecyclerViewTempAdapter : RecyclerView.Adapter<RecyclerViewTempAdapter.ReminderHolder>() {

    private val itemsList = mutableListOf<RcItem>()

    inner class ReminderHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ReminderRcItemBinding.bind(view)

        fun bind(item: RcItem) = with(binding) {
            reminderItemNameTextView.text = item.reminderName
            reminderItemTimeAndDateTextView.text = item.reminderTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.reminder_rc_item, parent, false
        )
        return ReminderHolder(view)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: ReminderHolder, position: Int) {
        holder.bind(itemsList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addReminderItem(item: RcItem) {
        itemsList.add(item)
        notifyDataSetChanged()
    }

}

data class RcItem(
    val reminderName: String,
    var reminderTime: String
)
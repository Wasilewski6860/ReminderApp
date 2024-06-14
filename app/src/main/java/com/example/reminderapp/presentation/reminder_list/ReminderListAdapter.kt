package com.example.reminderapp.presentation.reminder_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Task
import com.example.reminderapp.databinding.ReminderItemBinding
import java.text.DecimalFormat

class ReminderListAdapter(
    private val premiumActionListener: UserPremiumActionListener,
    private val blockActionListener: UserBlockActionListener
) : ListAdapter<Task, ReminderListAdapter.TaskViewHolder>(DiffCallBack) {

    class TaskViewHolder(val binding: ReminderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReminderItemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            switchIsActive.isActivated = item.isActive
            reminderNameTv.text = item.name
            reminderDescriptionTv.text = item.description
            reminderDescriptionTv.text = item.description
        }

    }


    interface UserPremiumActionListener {
        fun onClickItem(user: User)
    }

    interface UserBlockActionListener {
        fun onClickItem(user: User)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun roundFloat(value: Float, decimalPlaces: Int): Float {
        val decimalFormat = DecimalFormat("#.${"#".repeat(decimalPlaces)}")
        val format= decimalFormat.format(value).replace(",",".")
        return format.toFloat()
    }

}
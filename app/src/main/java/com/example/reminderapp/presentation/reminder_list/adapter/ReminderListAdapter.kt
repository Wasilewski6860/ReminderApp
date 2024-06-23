package com.example.reminderapp.presentation.reminder_list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.reminderapp.databinding.ReminderItemBinding
import com.example.reminderapp.utils.TimeDateUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderListAdapter(
    private val onItemClick: OnClickListener
) : ListAdapter<Task, ReminderListAdapter.TaskViewHolder>(DiffCallBack), KoinComponent {

    private val timeDateUtils : TimeDateUtils by inject()

    class TaskViewHolder(val binding: ReminderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReminderItemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            switchIsActive.setOnCheckedChangeListener(null)
            reminderDeleteIv.setOnClickListener(null)
            remindItemRootView.setOnClickListener(null)

            switchIsActive.isChecked = item.isActive
            reminderNameTv.text = item.name
            reminderDescriptionTv.text = item.description
            val formattedTime = timeDateUtils.getFormattedTime(item.reminderTime)
            if (formattedTime.isNullOrEmpty()){
                reminderDateTv.visibility = View.GONE
            }
            else{
                reminderDateTv.visibility = View.VISIBLE
                reminderDateTv.text = formattedTime
            }
            val period =  timeDateUtils.getFormattedPeriod(item.reminderTimePeriod)
            if (period.isNullOrEmpty()){
                reminderPeriodTv.visibility = View.GONE
            }
            else {
                reminderPeriodTv.visibility = View.VISIBLE
                reminderPeriodTv.text = period
            }

            reminderFlagIv.visibility = if (item.isMarkedWithFlag) View.VISIBLE else View.INVISIBLE
            reminderRepeatIv.visibility = if (item.type==TaskPeriodType.PERIODIC) View.VISIBLE else View.INVISIBLE

            remindItemRootView.setOnClickListener {
                onItemClick.onItemClick(item)
            }
            switchIsActive.setOnCheckedChangeListener { _, isChecked ->
                onItemClick.onSwitchClick(item, isChecked)
            }
            reminderDeleteIv.setOnClickListener {
                onItemClick.onDeleteClick(item)
            }
        }

    }


    interface OnClickListener {

        fun onItemClick(task: Task)

        fun onSwitchClick(task: Task, isChecked: Boolean)

        fun onDeleteClick(task: Task)

    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Task>() {

            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }


}
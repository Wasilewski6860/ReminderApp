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
    private val onItemClickListener: OnItemClickListener,
    private val onSwitchClickListener: OnSwitchClickListener,
    private val onDeleteClickListener: OnDeleteClickListener
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
            switchIsActive.isChecked = item.isActive
            reminderNameTv.text = item.name
            reminderDescriptionTv.text = item.description
            reminderDateTv.text = timeDateUtils.getFormattedTime(item.reminderTime, if (item.type==TaskPeriodType.PERIODIC) item.reminderTimePeriod else null)
            reminderFlagIv.visibility = if (item.isMarkedWithFlag) View.VISIBLE else View.INVISIBLE
            reminderRepeatIv.visibility = if (item.type==TaskPeriodType.PERIODIC) View.VISIBLE else View.INVISIBLE

            remindItemRootView.setOnClickListener {
                onItemClickListener.onClickItem(item)
            }
            reminderDeleteIv.setOnClickListener {
                onDeleteClickListener.onDeleteClick(item)
            }
            switchIsActive.setOnCheckedChangeListener { _, isChecked ->
                onSwitchClickListener.onClickItem(item, isChecked)
            }
        }

    }

    interface OnItemClickListener {
        fun onClickItem(task: Task)
    }

    interface OnSwitchClickListener {
        fun onClickItem(task: Task, isChecked: Boolean)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(task: Task)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Task>() {

            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }

}
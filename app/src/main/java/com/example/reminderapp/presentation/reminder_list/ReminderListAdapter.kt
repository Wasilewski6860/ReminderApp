package com.example.reminderapp.presentation.reminder_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.reminderapp.databinding.ReminderItemBinding
import com.example.reminderapp.notification.NotificationManager
import com.example.reminderapp.utils.TimeDateUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ReminderListAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onSwitchClickListener: OnSwitchClickListener
) : ListAdapter<Task, ReminderListAdapter.TaskViewHolder>(DiffCallBack), KoinComponent {

    val timeDateUtils : TimeDateUtils by inject()

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
            reminderDateTv.text = timeDateUtils.getFormattedTime(item.reminderTime, if (item.type==TaskPeriodType.PERIODIC) item.reminderTimePeriod else null)
            reminderFlagIv.visibility = if (item.isMarkedWithFlag) View.VISIBLE else View.INVISIBLE
            reminderRepeatIv.visibility = if (item.type==TaskPeriodType.PERIODIC) View.VISIBLE else View.INVISIBLE

            remindItemRootView.setOnClickListener {
                onItemClickListener.onClickItem(item)
            }

            switchIsActive.setOnCheckedChangeListener { view, isChecked ->
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
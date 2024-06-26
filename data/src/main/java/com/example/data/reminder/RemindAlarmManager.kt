package com.example.data.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.os.bundleOf
import com.example.data.reminder.Constants.ACTION_CANCEL_REMINDER
import com.example.data.reminder.Constants.ACTION_CREATE_REMINDER
import com.example.data.reminder.Constants.TASK_DESCRIPTION_EXTRA
import com.example.data.reminder.Constants.TASK_ID_EXTRA
import com.example.data.reminder.Constants.TASK_NAME_EXTRA
import com.example.domain.alarm.IRemindAlarmManager
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType

class RemindAlarmManager(
    private val context: Context,
    private val receiverClass: Class<*>
): IRemindAlarmManager {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun createAlarm(task: Task) {
        Log.d("MY LOG","RemindAlarmManager createAlarm")
        when(task.type) {
            TaskPeriodType.PERIODIC -> createAlarmPeriodic(
                title = task.name,
                text = task.description,
                startTimeInMs = task.reminderTime!!,
                intervalInMs = task.reminderTimePeriod!!,
                taskId = task.id
            )
            TaskPeriodType.ONE_TIME -> createAlarmOneTime(
                title = task.name,
                text = task.description,
                targetInMs = task.reminderTime!!,
                taskId = task.id
            )
            TaskPeriodType.NO_TIME -> Unit
        }
    }

    private fun createAlarmOneTime(title: String, text: String, targetInMs: Long,  taskId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.d("MY LOG","cannot schedule exact alarms")
                return
            }
        }
        Log.d("MY LOG","RemindAlarmManager createAlarmOneTime")
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            targetInMs,
            createReminderAlarmIntent(taskId, title, text)
        )
    }

    private fun createAlarmPeriodic(title: String, text: String, startTimeInMs: Long, intervalInMs: Long,  taskId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.d("MY LOG","RemindAlarmManager createAlarmPeriodic cannotScheduleExactAlarms")
                return
            }
        }

        Log.d("MY LOG","RemindAlarmManager createAlarmPeriodic setRepeating")
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startTimeInMs,
            intervalInMs,
            createReminderAlarmIntent(taskId, title, text)
        )
    }

    override fun clearAlarm(id: Int, name: String, description: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return
            }
        }
        Log.d("MY LOG","RemindAlarmManager clearAlarm")
        alarmManager.cancel(createReminderAlarmIntent(id, name, description))

        val intent = Intent(context, receiverClass).apply{
            putExtra(TASK_ID_EXTRA, id)
            putExtra(TASK_NAME_EXTRA, name)
            putExtra(TASK_DESCRIPTION_EXTRA, description)
            action = ACTION_CANCEL_REMINDER
        }
        context.sendBroadcast(intent)
    }

    override fun clearAlarm(task: Task) = clearAlarm(task.id, task.name, task.description)

    private fun createReminderAlarmIntent(taskId: Int, name: String, text: String): PendingIntent {
        val intent = Intent(context, receiverClass).apply {
            putExtra(TASK_ID_EXTRA, taskId)
            putExtra(TASK_NAME_EXTRA, name)
            putExtra(TASK_DESCRIPTION_EXTRA, text)
            action = ACTION_CREATE_REMINDER
        }
        return PendingIntent
            .getBroadcast(context, taskId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

}
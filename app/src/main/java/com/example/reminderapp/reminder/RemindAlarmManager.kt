package com.example.reminderapp.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.reminderapp.notification.Constants.TASK_DESCRIPTION_EXTRA
import com.example.reminderapp.notification.Constants.TASK_ID_EXTRA
import com.example.reminderapp.notification.Constants.TASK_NAME_EXTRA
import com.example.reminderapp.receivers.AlarmBroadcastReceiver

class RemindAlarmManager(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun createAlarm(task: Task) {
        Log.d("MY LOG","RemindAlarmManager createAlarm")
        when(task.type) {
            TaskPeriodType.PERIODIC -> createAlarmPeriodic(
                title = task.name,
                text = task.description,
                startTimeInMs = task.timestamp,
                intervalInMs = task.timeTarget,
                taskId = task.id
            )
            TaskPeriodType.ONE_TIME -> createAlarmOneTime(
                title = task.name,
                text = task.description,
                targetInMs = task.timeTarget,
                taskId = task.id
            )
        }
    }

    private fun createAlarmOneTime(title: String, text: String, targetInMs: Long,  taskId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return
            }
        }
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
            startTimeInMs + intervalInMs,
            intervalInMs,
            createReminderAlarmIntent(taskId, title, text)
        )
    }

    fun clearAlarm(task: Task){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return
            }
        }
        alarmManager.cancel(createReminderAlarmIntent(task.id, task.name, task.description))
    }

    private fun createReminderAlarmIntent(taskId: Int, name: String, text: String): PendingIntent {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(TASK_ID_EXTRA, taskId)
            putExtra(TASK_NAME_EXTRA, name)
            putExtra(TASK_DESCRIPTION_EXTRA, text)
        }
        return PendingIntent
            .getBroadcast(context, taskId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

}
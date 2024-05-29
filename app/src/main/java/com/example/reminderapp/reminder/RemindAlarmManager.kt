package com.example.reminderapp.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.reminderapp.notification.Constants.TASK_DESCRIPTION_EXTRA
import com.example.reminderapp.notification.Constants.TASK_ID_EXTRA
import com.example.reminderapp.notification.Constants.TASK_NAME_EXTRA
import com.example.reminderapp.notification.Constants.TASK_TYPE_EXTRA
import com.example.reminderapp.notification.Constants.TASK_TYPE_ONE_TIME_EXTRA
import com.example.reminderapp.notification.Constants.TASK_TYPE_PERIODIC_EXTRA

class RemindAlarmManager(
    private val alarmManager: AlarmManager,
    private val context: Context
) {

    fun createAlarmOneTime(title: String, text: String, targetInMs: Long,  taskId: Int) {
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

    fun createAlarmPeriodic(title: String, text: String, startTimeInMs: Long, intervalInMs: Long,  taskId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return
            }
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startTimeInMs + intervalInMs,
            intervalInMs,
            createReminderAlarmIntent(taskId, title, text)
        )
    }

    fun clearAlarm(taskId: Int, name: String, text: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return
            }
        }
        alarmManager.cancel(createReminderAlarmIntent(taskId, name, text))
    }

    private fun createReminderAlarmIntent(taskId: Int, name: String, text: String): PendingIntent {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(TASK_ID_EXTRA, taskId)
            putExtra(TASK_NAME_EXTRA, name)
            putExtra(TASK_DESCRIPTION_EXTRA, text)
        }
        return PendingIntent
            .getBroadcast(context, taskId.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)
    }

}
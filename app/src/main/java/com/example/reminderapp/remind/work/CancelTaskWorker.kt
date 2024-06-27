package com.example.reminderapp.remind.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.reminderapp.notification.NotificationManager
import com.example.data.reminder.RemindAlarmManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CancelTaskWorker(val context: Context, val workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams), KoinComponent {

    private val remindAlarmManager: RemindAlarmManager by inject()
    val notificationManager : NotificationManager by inject()

    override suspend fun doWork(): Result {
        Log.d("MY LOG","CancelTaskWorker doWork")
        val id = inputData.getInt("id",-1)
        val name = inputData.getString("name")
        val description = inputData.getString("description")
        if (id != -1 && name != null && description!=null) {
            remindAlarmManager.clearAlarm(id, name, description)
            notificationManager.clearNotification(id)
        }

        return Result.success()
    }

}
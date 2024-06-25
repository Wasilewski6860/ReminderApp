package com.example.reminderapp.remind.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.GetTaskUseCase
import com.example.reminderapp.notification.NotificationManager
import com.example.data.reminder.RemindAlarmManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PostponeTaskWorker(val context: Context, val workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams), KoinComponent {

    val notificationManager : NotificationManager by inject()

    override suspend fun doWork(): Result {
        Log.d("MY LOG","PostponeTaskWorker doWork")
        val id = inputData.getInt("id",-1)
        if (id != -1) {
            notificationManager.clearNotification(id)
        }

        return Result.success()
    }

}
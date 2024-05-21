package com.example.reminderapp.reminder.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.reminderapp.notification.NotificationManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CancelTaskWorker(val context: Context, val workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams), KoinComponent {

    val deleteTaskUseCase : DeleteTaskUseCase by inject()
    val remindWorkManager : RemindWorkManager by inject()
    val notificationManager : NotificationManager by inject()

    override suspend fun doWork(): Result {
        val id = inputData.getInt("id",-1)
        if (id != -1) {
            deleteTaskUseCase.execute(id)
            remindWorkManager.cancelWork(id)
            notificationManager.clearNotification(id)
        }
        return Result.success()
    }

}
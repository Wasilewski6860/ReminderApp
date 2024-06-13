package com.example.reminderapp.reminder.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.GetTaskUseCase
import com.example.reminderapp.notification.NotificationManager
import com.example.reminderapp.reminder.RemindAlarmManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CancelTaskWorker(val context: Context, val workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams), KoinComponent {

    val deleteTaskUseCase : DeleteTaskUseCase by inject()
    private val getTaskUseCase: GetTaskUseCase by inject()
    private val remindAlarmManager: RemindAlarmManager by inject()
    val notificationManager : NotificationManager by inject()

    override suspend fun doWork(): Result {
        Log.d("MY LOG","CancelTaskWorker doWork")
        val id = inputData.getInt("id",-1)
        if (id != -1) {
            getTaskUseCase.execute(id).collect{
                remindAlarmManager.clearAlarm(it)
                deleteTaskUseCase.execute(id)
                notificationManager.clearNotification(id)
            }
        }

        return Result.success()
    }

}
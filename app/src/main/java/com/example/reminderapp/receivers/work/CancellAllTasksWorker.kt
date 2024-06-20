package com.example.reminderapp.receivers.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.reminder.RemindAlarmManager
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.GetAllTasksUseCase
import com.example.reminderapp.notification.NotificationManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CancellAllTasksWorker(val context: Context, val workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams), KoinComponent {

    val getAllTasksUseCase : GetAllTasksUseCase by inject()
    val deleteTaskUseCase : DeleteTaskUseCase by inject()

    override suspend fun doWork(): Result {
        getAllTasksUseCase(Unit).collect{
            for(task in it) {
                deleteTaskUseCase(task.id)
            }
        }

        return Result.success()
    }

}
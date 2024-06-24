package com.example.reminderapp.remind.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.reminder.RemindAlarmManager
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.reminderapp.notification.NotificationManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteTaskWorker(val context: Context, val workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams), KoinComponent {

    val deleteTaskUseCase : DeleteTaskUseCase by inject()

    override suspend fun doWork(): Result {
        Log.d("MY LOG","DeleteTaskWorker doWork")
        val id = inputData.getInt("id",-1)
        val name = inputData.getString("name")
        val description = inputData.getString("description")
        if (id != -1 && name != null && description!=null)  {
            deleteTaskUseCase(id)
        }

        return Result.success()
    }

}
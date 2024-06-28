package com.example.reminderapp.remind.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.domain.use_case.reminder.DeleteReminderUseCase
import com.example.domain.use_case.task.DeleteTaskUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteTaskWorker(val context: Context, val workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams), KoinComponent {

    val deleteReminderUseCase : DeleteReminderUseCase by inject()

    override suspend fun doWork(): Result {
        Log.d("MY LOG","DeleteTaskWorker doWork")
        val id = inputData.getInt("id",-1)
        if (id != -1){
            deleteReminderUseCase(id)
        }
        return Result.success()
    }

}
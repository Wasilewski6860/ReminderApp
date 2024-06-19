package com.example.reminderapp.reminder.work

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class RemindWorkManager(val context: Context) {

    fun createCancelWorkRequest(taskId: Int) {
        val workRequest = OneTimeWorkRequestBuilder<CancelTaskWorker>()
            .addTag(taskId.toString())
            .setInputData(
                workDataOf(
                    "id" to taskId,
                )
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(taskId.toString(),
            ExistingWorkPolicy.REPLACE, workRequest)
    }

    fun createSetInactiveWorkRequest(taskId: Int) {
        val workRequest = OneTimeWorkRequestBuilder<SetInactiveTaskWorker>()
            .addTag(taskId.toString())
            .setInputData(
                workDataOf(
                    "id" to taskId,
                )
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(taskId.toString(),
            ExistingWorkPolicy.REPLACE, workRequest)
    }

    fun createPostponeWorkRequest(taskId: Int) {
        val workRequest = OneTimeWorkRequestBuilder<PostponeTaskWorker>()
            .addTag(taskId.toString())
            .setInputData(
                workDataOf(
                    "id" to taskId,
                )
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(taskId.toString(),
            ExistingWorkPolicy.REPLACE, workRequest)
    }

    /**
     * Использовать для отмены напоминания
     * @param taskId Это id напоминания(id таска), который отменяется
     */
    fun cancelWork(taskId: Int){
        WorkManager.getInstance(context).cancelAllWorkByTag(taskId.toString())
    }
}
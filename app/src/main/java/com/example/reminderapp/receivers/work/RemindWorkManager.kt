package com.example.reminderapp.receivers.work

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class RemindWorkManager(val context: Context) {

    fun createCancelWorkRequest(id: Int, name: String, description: String) {
        val workRequest = OneTimeWorkRequestBuilder<CancelTaskWorker>()
            .addTag(id.toString())
            .setInputData(
                workDataOf(
                    "id" to id,
                    "name" to name,
                    "description" to description,
                )
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(id.toString(),
            ExistingWorkPolicy.REPLACE, workRequest)
    }

    fun createCancelAllWorkRequest() {
        val workRequest = OneTimeWorkRequestBuilder<CancellAllTasksWorker>()
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun createSetInactiveWorkRequest(id: Int, name: String, description: String) {
        val workRequest = OneTimeWorkRequestBuilder<ChangeTaskActivityWorker>()
            .addTag(id.toString())
            .setInputData(
                workDataOf(
                    "id" to id,
                    "name" to name,
                    "description" to description,
                )
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(id.toString(),
            ExistingWorkPolicy.REPLACE, workRequest)
    }

    fun createPostponeWorkRequest(id: Int, name: String, description: String) {
        val workRequest = OneTimeWorkRequestBuilder<PostponeTaskWorker>()
            .addTag(id.toString())
            .setInputData(
                workDataOf(
                    "id" to id,
                    "name" to name,
                    "description" to description,
                )
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(id.toString(),
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
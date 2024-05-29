package com.example.reminderapp.reminder.work

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit



class RemindWorkManager(val context: Context) {

    /**
     * Использовать при создании периодических напоминаний.
     * @param title будет указан как title уведомления
     * @param text будет указан как основной текст уведомления уведомления
     * @param startTimeDelayInSeconds является временем в секундах, через которое периодическое уведомления появится в первый раз(если мы хотим, чтобы, например, оно было показано через три дня(в некоторый определенный день), а после повторялось каждые два дня)
     * @param startTimeDelayInSeconds является временем в секундах, через которое периодическое уведомления будет появляться регулярно(два дня в примере выше)
     * @param taskId Это id экземпляра класса Task, которому будет соответствовать создаваемое напоминание.
     *
     * Для изменения существующего периодического напоминания сначала вызвать cancelWork(taskId), а потом этот метод
     */
    fun createPeriodicWorkRequest(title: String,text: String,startTimeDelayInSeconds: Long, timeDelayInMilliSeconds: Long, taskId: Int) {
        val workRequest =  PeriodicWorkRequestBuilder<RemindWorker>(timeDelayInMilliSeconds, TimeUnit.MILLISECONDS)
            .setInitialDelay(startTimeDelayInSeconds, TimeUnit.MILLISECONDS)
            .addTag(taskId.toString())
            .setInputData(
                workDataOf(
                    "title" to title,
                    "text" to text,
                    "id" to taskId,
                )
            )
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    /**
     * Использовать при создании единоразовых напоминаний.
     * @param title будет указан как title уведомления
     * @param text будет указан как основной текст уведомления уведомления
     * @param timeDelayInSeconds является временем в секундах, через которое напоминание будет показано
     * @param taskId Это id экземпляра класса Task, которому будет соответствовать создаваемое напоминание.
     *
     * Для изменения существующего напоминания достаточно вызвать этот метод с правильным taskId, тогда cancelWork(taskId) вызывать не нужно
     */
    fun createOneTimeWorkRequest(title: String,text: String,timeDelayInMilliSeconds: Long, taskId: Int) {
        val workRequest = OneTimeWorkRequestBuilder<RemindWorker>()
            .setInitialDelay(timeDelayInMilliSeconds, TimeUnit.MILLISECONDS)
            .addTag(taskId.toString())
            .setInputData(
                workDataOf(
                    "title" to title,
                    "text" to text,
                    "id" to taskId,
                )
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(taskId.toString(),
            ExistingWorkPolicy.REPLACE, workRequest)
    }

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

    /**
     * Использовать для отмены напоминания
     * @param taskId Это id напоминания(id таска), который отменяется
     */
    fun cancelWork(taskId: Int){
        WorkManager.getInstance(context).cancelAllWorkByTag(taskId.toString())
    }
}
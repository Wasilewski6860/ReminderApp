package com.example.reminderapp.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.reminderapp.notification.NotificationManager

class RemindWorker(val context: Context, val workerParams: WorkerParameters) : Worker(context,
    workerParams) {
    override fun doWork(): Result {

        NotificationManager(context).createNotification(
            inputData.getString("title").toString()!!,
            inputData.getString("text").toString()!!,
            inputData.getInt("id",1)
        )
        return Result.success()
    }

}
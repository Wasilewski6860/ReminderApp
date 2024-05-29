package com.example.reminderapp.reminder.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.reminderapp.notification.NotificationManager

class RemindWorker(val context: Context, val workerParams: WorkerParameters) : Worker(context,
    workerParams) {
    override fun doWork(): Result {

        NotificationManager(applicationContext).createNotification(
            inputData.getString("title").toString()!!,
            inputData.getString("text").toString()!!,
            inputData.getInt("id",1)
        )
        Log.d("REMIND_WORKER", "ID:"+id)
        return Result.success()
    }

}
package com.example.reminderapp.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.notification.Constants.ACTION_CANCEL_PENDING_INTENT_ID
import com.example.reminderapp.notification.Constants.ACTION_POSTPONE_PENDING_INTENT_ID
import com.example.reminderapp.notification.Constants.TASK_ID_EXTRA
import com.example.reminderapp.reminder.ReminderBroadcast

class NotificationManager(val context: Context) {

    private val CHANNEL_ID = "REMINDER_CHANNEL"

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Some name"
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT )
            val notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(contentTitle: String, contextText: String, taskId: Int) {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(contentTitle)
            .setContentText(contextText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

//            .addAction(postponeAction(taskId))
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java).also {
                        it.action = Constants.ACTION_SHOW_TASK
                        it.putExtra(TASK_ID_EXTRA,taskId )
                    },
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .addAction(cancelAction(taskId))
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(taskId,builder.build())
        }
    }

    private fun cancelAction(id: Int): NotificationCompat.Action {

        val intent = Intent(context, ReminderBroadcast::class.java)

        intent.putExtra(TASK_ID_EXTRA, id)

        val cancelReminderPendingIntent = PendingIntent.getBroadcast(
            context,
            ACTION_CANCEL_PENDING_INTENT_ID,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action(
            0,
            "Cancel",
            cancelReminderPendingIntent
        )
    }

    private fun postponeAction(id: Int): NotificationCompat.Action {
        val intent = Intent(Constants.DELETE_TASK)

        intent.putExtra(TASK_ID_EXTRA, id)

        val cancelReminderPendingIntent = PendingIntent.getBroadcast(
            context,
            ACTION_POSTPONE_PENDING_INTENT_ID,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action(
            R.drawable.ic_close,
            "Postpone some text",
            cancelReminderPendingIntent
        )
    }


    fun clearNotification(id: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }



}
package com.example.reminderapp.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.notification.Constants.ACTION_CANCEL_PENDING_INTENT_ID
import com.example.reminderapp.notification.Constants.ACTION_DISMISS
import com.example.reminderapp.notification.Constants.ACTION_POSTPONE
import com.example.reminderapp.notification.Constants.ACTION_POSTPONE_PENDING_INTENT_ID
import com.example.reminderapp.notification.Constants.TASK_ID_EXTRA
import com.example.reminderapp.receivers.ReminderBroadcast
import org.koin.core.component.KoinComponent

class NotificationManager(val context: Context) : KoinComponent {

    private val CHANNEL_ID = "REMINDER_CHANNEL"
    private val CHANNEL_NAME = context.getString(R.string.app_name)

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance )
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }
    }



    fun createNotification(contentTitle: String, contextText: String, taskId: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(contentTitle)
            .setContentText(contextText)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setOngoing(true)

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
            .addAction(getDismissAction(taskId))
            .addAction(getPostponeAction(taskId))

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(taskId,builder.build())
        }
    }

    private fun getDismissAction(id: Int): NotificationCompat.Action {

        val intent = Intent(context, ReminderBroadcast::class.java).putExtra(TASK_ID_EXTRA, id).setAction(ACTION_DISMISS)

        val cancelReminderPendingIntent = PendingIntent.getBroadcast(
            context,
            ACTION_CANCEL_PENDING_INTENT_ID,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action(
            R.drawable.ic_close,
            "Dismiss",
            cancelReminderPendingIntent
        )
    }


    private fun getPostponeAction(id: Int): NotificationCompat.Action {
        val intent = Intent(context, ReminderBroadcast::class.java).putExtra(TASK_ID_EXTRA, id).setAction(ACTION_POSTPONE)

        intent.putExtra(TASK_ID_EXTRA, id)

        val postponeReminderPendingIntent = PendingIntent.getBroadcast(
            context,
            ACTION_POSTPONE_PENDING_INTENT_ID,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action(
            R.drawable.ic_check,
            "Postpone",
            postponeReminderPendingIntent
        )
    }


    fun clearNotification(id: Int) {
        Log.d("NotificationManager","clearNotification")
        notificationManager.cancel(id)
    }



}
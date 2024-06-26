package com.example.reminderapp.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.data.reminder.Constants.ACTION_CANCEL_PENDING_INTENT_ID
import com.example.data.reminder.Constants.ACTION_POSTPONE
import com.example.data.reminder.Constants.ACTION_POSTPONE_PENDING_INTENT_ID
import com.example.data.reminder.Constants.TASK_ID_EXTRA
import com.example.reminderapp.remind.receivers.ReminderNotificationBroadcastReceiver
import com.example.data.reminder.Constants
import com.example.data.reminder.Constants.ACTION_DELETE
import com.example.data.reminder.Constants.ACTION_DISMISS
import com.example.data.reminder.Constants.TASK_DESCRIPTION_EXTRA
import com.example.data.reminder.Constants.TASK_EXTRA
import com.example.data.reminder.Constants.TASK_NAME_EXTRA
import org.koin.core.component.KoinComponent

class NotificationManager(val context: Context) : KoinComponent {

    private val CHANNEL_ID = "NOTIFICATION_CHANNEL"
    private val CHANNEL_NAME = context.getString(R.string.app_name)

    private val CHANNEL_REMINDER_ID = "REMINDER_CHANNEL"
    private val CHANNEL_REMINDER_NAME = context.getString(R.string.app_name)

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
        createNotificationReminderChannel()
    }

    fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotificationReminderChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_REMINDER_ID, CHANNEL_REMINDER_NAME, importance )
        channel.setBypassDnd(true)
        channel.setSound(null, null)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotificationReminder(contentTitle: String, contextText: String, taskId: Int) {
        Log.d("NotificationManager","createNotificationReminder taskId:"+taskId.toString()+" name:"+contentTitle+" desc:"+contextText )
        val builder = NotificationCompat.Builder(context, CHANNEL_REMINDER_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(contentTitle)
            .setContentText(contextText)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(
                PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(TASK_ID_EXTRA, taskId)
                }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE),
                true
            )

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(taskId,builder.build())
        }
    }
    fun createNotification(contentTitle: String, contextText: String, taskId: Int) {
        Log.d("NotificationManager","createNotification taskId:"+taskId.toString()+" name:"+contentTitle+" desc:"+contextText )
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
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
            .addAction(getDismissAction(taskId, contentTitle, contextText))
            .addAction(getPostponeAction(taskId, contentTitle, contextText))

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(taskId,builder.build())
        }
    }

    private fun getDismissAction(id: Int, name: String, description: String): NotificationCompat.Action {

        val intent = Intent(context, ReminderNotificationBroadcastReceiver::class.java).putExtra(
            TASK_EXTRA,
            bundleOf(
                    TASK_ID_EXTRA to id,
                    TASK_NAME_EXTRA to name,
                    TASK_DESCRIPTION_EXTRA to description
                )
        ).setAction(ACTION_DISMISS)

        val cancelReminderPendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action(
            R.drawable.ic_close,
            context.getString(R.string.dismiss),
            cancelReminderPendingIntent
        )
    }


    private fun getPostponeAction(id: Int, name: String, description: String): NotificationCompat.Action {
        val intent = Intent(context, ReminderNotificationBroadcastReceiver::class.java).putExtra(
            TASK_EXTRA,
            bundleOf(
                TASK_ID_EXTRA to id,
                TASK_NAME_EXTRA to name,
                TASK_DESCRIPTION_EXTRA to description
            )
        ).setAction(ACTION_POSTPONE)

        intent.putExtra(TASK_ID_EXTRA, id)

        val postponeReminderPendingIntent = PendingIntent.getBroadcast(
            context,
            ACTION_POSTPONE_PENDING_INTENT_ID,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action(
            R.drawable.ic_check,
            context.getString(R.string.postpone),
            postponeReminderPendingIntent
        )
    }


    fun clearNotification(id: Int) {
        try {
            Log.d("NotificationManager","clearNotification")
            notificationManager.cancel(id)
        }
        catch (e: Exception) {
            Log.d("NotificationManager clearNotification exception",e.toString())
        }

    }

    fun clearNotifications() {
        notificationManager.cancelAll()
    }



}
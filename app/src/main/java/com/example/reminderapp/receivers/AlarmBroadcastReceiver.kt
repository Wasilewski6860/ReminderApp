package com.example.reminderapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.example.reminderapp.utils.Constants
import com.example.reminderapp.notification.NotificationManager

class AlarmBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, Intent: Intent?) {
        Log.d("MY LOG", "AlarmBroadcastReceiver onReceive")
        val id = Intent?.extras?.getInt(Constants.TASK_ID_EXTRA)
        val title = Intent?.extras?.getString(Constants.TASK_NAME_EXTRA)
        val description = Intent?.extras?.getString(Constants.TASK_DESCRIPTION_EXTRA)
        if (context != null && id != null && title != null && description != null) {

            if ((context.getSystemService(Context.POWER_SERVICE) as PowerManager).isScreenOn) {
                Log.d("MY LOG", "AlarmBroadcastReceiver onReceive success")
                NotificationManager(context).createNotification(title, description, id)
            }
            else {
                NotificationManager(context).createNotificationReminder(title, description, id)
            }
        }
        Log.d("ALARM_RECEIVER", "ID:"+id)
    }


}
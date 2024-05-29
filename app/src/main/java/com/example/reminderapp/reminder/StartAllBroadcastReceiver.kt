package com.example.reminderapp.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.reminderapp.notification.Constants
import com.example.reminderapp.reminder.work.RemindWorkManager

class StartAllBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, Intent: Intent?) {
        val id = Intent?.extras?.getInt(Constants.TASK_ID_EXTRA)
        if (context != null && id != null) {
            RemindWorkManager(context).createCancelWorkRequest(id)
        }
    }

}
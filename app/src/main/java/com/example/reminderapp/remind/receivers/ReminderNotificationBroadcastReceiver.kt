package com.example.reminderapp.remind.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.data.reminder.Constants
import com.example.data.reminder.Constants.TASK_DESCRIPTION_EXTRA
import com.example.data.reminder.Constants.TASK_EXTRA
import com.example.data.reminder.Constants.TASK_ID_EXTRA
import com.example.data.reminder.Constants.TASK_NAME_EXTRA
import com.example.reminderapp.remind.work.RemindWorkManager
import org.koin.core.component.KoinComponent

class ReminderNotificationBroadcastReceiver: BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MY LOG","ReminderNotificationBroadcastReceiver onReceive")
        val bundle = intent.getBundleExtra(TASK_EXTRA)
        val id = bundle?.getInt(TASK_ID_EXTRA)
        val name = bundle?.getString(TASK_NAME_EXTRA)
        val description = bundle?.getString(TASK_DESCRIPTION_EXTRA)
        val workManager = RemindWorkManager(context)
        if (id != null && name != null && description != null) {
            when (intent.action) {
                Constants.ACTION_DISMISS -> {
                    Log.d("MY LOG","ReminderNotificationBroadcastReceiver ACTION_DISMISS")
                    workManager.createDismissWorkRequest(id)
                }
                Constants.ACTION_POSTPONE -> {
                    Log.d("MY LOG","ReminderNotificationBroadcastReceiver ACTION_POSTPONE")
                    workManager.createPostponeWorkRequest(id)
                }
            }
        }
        if (intent.action==Constants.ACTION_CANCEL_ALL) {
            Log.d("MY LOG","ReminderNotificationBroadcastReceiver ACTION_CANCEL_ALL")
            workManager.createCancelAllWorkRequest()
        }
    }

}
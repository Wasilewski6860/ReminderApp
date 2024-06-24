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

class ReminderBroadcast: BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context?, Intent: Intent?) {
        Log.d("MY LOG","ReminderBroadcast onReceive")
        val bundle = Intent?.getBundleExtra(TASK_EXTRA)
        val id = bundle?.getInt(TASK_ID_EXTRA)
        val name = bundle?.getString(TASK_NAME_EXTRA)
        val description = bundle?.getString(TASK_DESCRIPTION_EXTRA)

        val workManager = context?.let { RemindWorkManager(it) }
        if (workManager != null && id != null && name != null && description != null) {
            when (Intent?.action) {
                Constants.ACTION_DISMISS -> {
                    Log.d("MY LOG","ReminderBroadcast onReceive ACTION_DISMISS")
                    workManager.createCancelWorkRequest(id,name, description )
                }
                Constants.ACTION_DELETE -> {
                    Log.d("MY LOG","ReminderBroadcast onReceive ACTION_DELETE")
                    workManager.createDeleteWorkRequest(id,name, description )
                }
                Constants.ACTION_POSTPONE -> {
                    Log.d("MY LOG","ReminderBroadcast onReceive ACTION_POSTPONE")
                    workManager.createPostponeWorkRequest(id,name, description)
                }
                Constants.ACTION_SET_INACTIVE -> {
                    workManager.createSetInactiveWorkRequest(id,name, description)
                }
            }
        }
        if (Intent?.action==Constants.ACTION_CANCEL_ALL) {
            workManager?.createCancelAllWorkRequest()
        }
    }

}
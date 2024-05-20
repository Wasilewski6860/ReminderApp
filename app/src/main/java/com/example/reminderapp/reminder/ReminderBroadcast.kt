package com.example.reminderapp.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.reminderapp.notification.Constants.TASK_ID_EXTRA
import com.example.reminderapp.reminder.work.RemindWorkManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// В компоненте не жизненного цикла необходим KoinComponent
class ReminderBroadcast: BroadcastReceiver() {

    override fun onReceive(context: Context?, Intent: Intent?) {
        val id = Intent?.extras?.getInt(TASK_ID_EXTRA)
        if (context != null && id != null) {
            RemindWorkManager(context).createCancelWorkRequest(id)
        }
    }

}
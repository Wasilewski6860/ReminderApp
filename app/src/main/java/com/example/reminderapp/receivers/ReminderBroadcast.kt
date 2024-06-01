package com.example.reminderapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.GetTaskUseCase
import com.example.reminderapp.notification.Constants
import com.example.reminderapp.notification.Constants.TASK_ID_EXTRA
import com.example.reminderapp.reminder.RemindAlarmManager
import com.example.reminderapp.reminder.work.RemindWorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderBroadcast: BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context?, Intent: Intent?) {
        Log.d("MY LOG","ReminderBroadcast onReceive")
        val id = Intent?.extras?.getInt(TASK_ID_EXTRA)
        if (context != null && id != null) {
            val workManager = RemindWorkManager(context)
            when (Intent.action) {
                Constants.ACTION_DISMISS -> {
                    Log.d("MY LOG","ReminderBroadcast onReceive ACTION_DISMISS")
                    workManager.createCancelWorkRequest(id)
                }
                Constants.ACTION_POSTPONE -> {
                    Log.d("MY LOG","ReminderBroadcast onReceive ACTION_POSTPONE")
                    workManager.createPostponeWorkRequest(id)
                }
            }
        }
    }

}
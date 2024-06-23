
package com.example.reminderapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.domain.use_case.GetAllTasksUseCase
import com.example.reminderapp.presentation.base.UiState
import com.example.data.reminder.RemindAlarmManager
import com.example.domain.model.TaskPeriodType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeviceBootReceiver : BroadcastReceiver(), KoinComponent {

    private val remindAlarmManager: RemindAlarmManager by inject()
    private val getAllTasksUseCase: GetAllTasksUseCase by inject()
    private val scope = CoroutineScope(SupervisorJob())

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED" ||
            intent?.action == "android.intent.action.LOCKED_BOOT_COMPLETED") {
            Log.d("MY LOG","DeviceBootReceiver onReceive")
            resetAlarm()
        }
    }

    private fun resetAlarm() {
        scope.launch(Dispatchers.Main) {
            getAllTasksUseCase(Unit)
                .collect { tasks ->
                    tasks.forEach { task ->
                        if(task.isActive && task.type != TaskPeriodType.NO_TIME) remindAlarmManager.createAlarm(task)
                    }
                }
        }
    }

}

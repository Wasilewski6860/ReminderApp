package com.example.domain.use_case.alarm

import com.example.domain.alarm.IRemindAlarmManager
import com.example.domain.model.Task

class ChangeAlarmUseCase(
    private val createAlarmUseCase: CreateAlarmUseCase,
    private val clearAlarmUseCase: ClearAlarmUseCase
) {
    operator fun invoke(task: Task) {
        if (task.isActive) {
            createAlarmUseCase(task)
        }
        else {
            clearAlarmUseCase(task)
        }

    }
}
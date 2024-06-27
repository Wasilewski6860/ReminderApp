package com.example.domain.use_case.alarm

import com.example.domain.alarm.IRemindAlarmManager
import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.repository.IGroupRepository

class CreateAlarmUseCase(private val alarmManager: IRemindAlarmManager) {
    operator fun invoke(task: Task) = alarmManager.createAlarm(task)
}
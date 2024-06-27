package com.example.domain.use_case.alarm

import com.example.domain.alarm.IRemindAlarmManager
import com.example.domain.model.Task

//Вызываем когда хотим отменить задачу и очистить уведомление
// ACTION_SET_INACTIVE, ACTION_DISMISS делали то же самое, отменяли задачу и удаляли уведомление
class ClearAlarmUseCase(private val alarmManager: IRemindAlarmManager) {
    operator fun invoke(task: Task) = alarmManager.clearAlarm(task)
    operator fun invoke(taskId: Int, taskName: String, taskDescription: String) = alarmManager.clearAlarm(taskId, taskName, taskDescription)
}
package com.example.domain.alarm

import com.example.domain.model.Task

interface IRemindAlarmManager {
    fun createAlarm(task: Task)
    fun clearAlarm(taskId: Int, taskName: String, taskDescription: String)
    fun clearAlarm(task: Task)
}
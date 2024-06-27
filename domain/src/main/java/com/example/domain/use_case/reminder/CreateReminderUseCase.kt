package com.example.domain.use_case.reminder

import com.example.domain.model.Task
import com.example.domain.use_case.alarm.CreateAlarmUseCase
import com.example.domain.use_case.task.SaveTaskUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class CreateReminderUseCase(private val saveTaskUseCase: SaveTaskUseCase, private val createAlarmUseCase: CreateAlarmUseCase) {
    suspend operator fun invoke(task: Task): Flow<Task> {
        val result = saveTaskUseCase(task).stateIn(GlobalScope)
        val data = result.first()
        createAlarmUseCase(data)
        return result
    }
}
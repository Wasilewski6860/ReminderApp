package com.example.domain.use_case.reminder

import com.example.domain.model.Task
import com.example.domain.use_case.alarm.ClearAlarmUseCase
import com.example.domain.use_case.task.DeleteTaskUseCase
import com.example.domain.use_case.task.GetTaskUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class DeleteReminderUseCase(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val clearAlarmUseCase: ClearAlarmUseCase
) {
    suspend operator fun invoke(task: Task) {
        clearAlarmUseCase(task)
        deleteTaskUseCase(task)
    }
    suspend operator fun invoke(id: Int) {
        val result = getTaskUseCase(id).stateIn(GlobalScope).first()
        clearAlarmUseCase(result)
        deleteTaskUseCase(result)
    }
}
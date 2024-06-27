package com.example.domain.use_case.reminder

import com.example.domain.model.Task
import com.example.domain.use_case.alarm.ChangeAlarmUseCase
import com.example.domain.use_case.task.EditTaskUseCase

class EditReminderUseCase(private val editTaskUseCase: EditTaskUseCase, private val changeAlarmUseCase: ChangeAlarmUseCase) {
    suspend operator fun invoke(task: Task) {
        changeAlarmUseCase(task)
        editTaskUseCase(task)
    }
}
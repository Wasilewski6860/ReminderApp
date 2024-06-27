package com.example.domain.use_case.reminder

import com.example.domain.use_case.group.DeleteGroupUseCase
import com.example.domain.use_case.group.GetGroupWithTasksUseCase
import kotlinx.coroutines.flow.first

class DeleteReminderGroupUseCase(private val getGroupWithTasksUseCase: GetGroupWithTasksUseCase, private val deleteReminderUseCase: DeleteReminderUseCase, private val deleteGroupUseCase: DeleteGroupUseCase) {
    suspend operator fun invoke(id: Int) {
        val result = getGroupWithTasksUseCase(id).first()
        result.tasks.forEach { task ->
            deleteReminderUseCase(task)
        }
        deleteGroupUseCase(id)
    }
}
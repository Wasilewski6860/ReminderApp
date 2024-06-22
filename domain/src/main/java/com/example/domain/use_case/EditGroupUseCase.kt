package com.example.domain.use_case

import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

typealias EditGroupBaseUseCase = BaseUseCase<Group, Unit>

class EditGroupUseCase(private val taskRepository: TaskRepository) : EditGroupBaseUseCase {
    override suspend fun invoke(group: Group)  = taskRepository.editGroup(group)

}

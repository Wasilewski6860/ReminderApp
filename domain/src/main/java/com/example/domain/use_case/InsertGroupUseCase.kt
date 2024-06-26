package com.example.domain.use_case

import com.example.domain.model.Group
import com.example.domain.repository.TaskRepository

typealias InsertGroupBaseUseCase = BaseUseCase<Group, Long>

class InsertGroupUseCase(private val taskRepository: TaskRepository) : InsertGroupBaseUseCase {
    override suspend fun invoke(group: Group)  = taskRepository.addGroup(group)
}
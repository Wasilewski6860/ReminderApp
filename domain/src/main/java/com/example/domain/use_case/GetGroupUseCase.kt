package com.example.domain.use_case

import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow


typealias GetGroupBaseUseCase = BaseUseCase<Int, Flow<Group>>

class GetGroupUseCase(private val taskRepository: TaskRepository) : GetGroupBaseUseCase {
    override suspend fun invoke(id: Int)  = taskRepository.getGroup(id)
}
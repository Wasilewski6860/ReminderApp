package com.example.domain.use_case

import com.example.domain.model.GroupWithTasks
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow


typealias GetGroupWithTasksBaseUseCase = BaseUseCase<Int, Flow<GroupWithTasks>>

class GetGroupWithTasksUseCase(private val taskRepository: TaskRepository) : GetGroupWithTasksBaseUseCase {
    override suspend fun invoke(id: Int)  = taskRepository.getGroupWithTasks(id)
}
package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetNoTimeTasksBaseUseCase = BaseUseCase<Unit, Flow<List<Task>>>

class GetNoTimeTasksUseCase(private val taskRepository: TaskRepository) : GetNoTimeTasksBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getNoTimeTasks()
}
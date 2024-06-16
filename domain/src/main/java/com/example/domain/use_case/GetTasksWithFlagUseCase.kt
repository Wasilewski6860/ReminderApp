package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetTasksWithFlagBaseUseCase = BaseUseCase<Unit, Flow<List<Task>>>

class GetTasksWithFlagUseCase(private val taskRepository: TaskRepository) : GetTasksWithFlagBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getTasksWithFlag()
}
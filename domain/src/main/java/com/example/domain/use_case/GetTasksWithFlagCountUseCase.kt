package com.example.domain.use_case

import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetTasksWithFlagCountBaseUseCase = BaseUseCase<Unit, Flow<Int>>

class GetTasksWithFlagCountUseCase(private val taskRepository: TaskRepository) : GetTasksWithFlagCountBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getTasksWithFlagCount()
}
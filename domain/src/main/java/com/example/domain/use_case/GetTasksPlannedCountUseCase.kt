package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetTasksPlannedCountBaseUseCase = BaseUseCase<Unit, Flow<Int>>

class GetTasksPlannedCountUseCase(private val taskRepository: TaskRepository) : GetTasksPlannedCountBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getTasksPlannedCount()
}
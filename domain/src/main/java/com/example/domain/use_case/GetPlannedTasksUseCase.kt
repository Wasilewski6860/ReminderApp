package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetPlannedTasksBaseUseCase = BaseUseCase<Unit, Flow<List<Task>>>

class GetPlannedTasksUseCase(private val taskRepository: TaskRepository) : GetPlannedTasksBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getTasksPlanned()
}
package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetAllTasksBaseUseCase = BaseUseCase<Unit, Flow<List<Task>>>

class GetAllTasksUseCase(private val taskRepository: TaskRepository) : GetAllTasksBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getAllTasks()
}
package com.example.domain.use_case

import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetAllOneTimeTasksBaseUseCase = BaseUseCase<Unit, Flow<List<Task>>>

class GetAllOneTimeTasksUseCase(private val taskRepository: TaskRepository) : GetAllOneTimeTasksBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getAllTasksByPeriodType(TaskPeriodType.ONE_TIME)
}

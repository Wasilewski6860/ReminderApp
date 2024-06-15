package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetAllPeriodicTasksBaseUseCase = BaseUseCase<Unit, Flow<List<Task>>>

class GetAllPeriodicTasksUseCase(private val taskRepository: TaskRepository) : GetAllPeriodicTasksBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getAllTasksByPeriodType(TaskPeriodType.PERIODIC)
}

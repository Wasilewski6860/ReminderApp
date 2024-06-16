package com.example.domain.use_case

import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetTasksForTodayCountBaseUseCase = BaseUseCase<Unit, Flow<Int>>

class GetTasksForTodayCountUseCase(private val taskRepository: TaskRepository) : GetTasksForTodayCountBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getTasksForTodayCount()
}
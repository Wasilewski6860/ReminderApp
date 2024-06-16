package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetTasksForTodayBaseUseCase = BaseUseCase<Unit, Flow<List<Task>>>

class GetTasksForTodayUseCase(private val taskRepository: TaskRepository) : GetTasksForTodayBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getTasksForToday()
}
package com.example.domain.use_case

import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetAllTasksCountBaseUseCase = BaseUseCase<Unit, Flow<Int>>

class GetAllTasksCountUseCase(private val taskRepository: TaskRepository) : GetAllTasksCountBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getAllTasksCount()
}
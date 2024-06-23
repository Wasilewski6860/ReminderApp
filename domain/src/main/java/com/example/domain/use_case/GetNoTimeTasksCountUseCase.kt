package com.example.domain.use_case

import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetNoTimeTasksCountBaseUseCase = BaseUseCase<Unit, Flow<Int>>

class GetNoTimeTasksCountUseCase(private val taskRepository: TaskRepository) : GetNoTimeTasksCountBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getCountOfNoTimeTasks()
}
package com.example.domain.use_case

import com.example.domain.repository.TaskRepository

typealias ClearAllTasksBaseUseCase = BaseUseCase<Unit, Unit>

class ClearAllTasksUseCase(private val taskRepository: TaskRepository) : ClearAllTasksBaseUseCase {
    override suspend operator fun invoke(params: Unit) = taskRepository.deleteAll()
}

package com.example.domain.use_case.task

import com.example.domain.repository.ITaskRepository

class ClearAllTasksUseCase(private val taskRepository: ITaskRepository) {
    suspend operator fun invoke() = taskRepository.deleteAll()
}
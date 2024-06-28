package com.example.domain.use_case.task

import com.example.domain.repository.ITaskRepository

class GetAllTasksUseCase(private val taskRepository: ITaskRepository) {
    operator fun invoke() = taskRepository.getAllTasks()
}
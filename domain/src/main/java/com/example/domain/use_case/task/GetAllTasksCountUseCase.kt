package com.example.domain.use_case.task

import com.example.domain.repository.ITaskRepository

class GetAllTasksCountUseCase(private val taskRepository: ITaskRepository) {
    operator fun invoke() = taskRepository.getAllTasksCount()
}
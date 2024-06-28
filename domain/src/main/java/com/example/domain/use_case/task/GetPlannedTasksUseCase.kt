package com.example.domain.use_case.task

import com.example.domain.repository.ITaskRepository

class GetPlannedTasksUseCase(private val taskRepository: ITaskRepository) {
    operator fun invoke() = taskRepository.getTasksPlanned()
}
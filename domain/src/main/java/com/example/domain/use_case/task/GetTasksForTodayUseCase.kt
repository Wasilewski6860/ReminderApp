package com.example.domain.use_case.task

import com.example.domain.repository.ITaskRepository

class GetTasksForTodayUseCase(private val taskRepository: ITaskRepository) {
    operator fun invoke() = taskRepository.getTasksForToday()
}
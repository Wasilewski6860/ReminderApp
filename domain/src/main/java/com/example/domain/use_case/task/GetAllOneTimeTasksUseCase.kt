package com.example.domain.use_case.task

import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.ITaskRepository

class GetAllOneTimeTasksUseCase(private val taskRepository: ITaskRepository) {
    operator fun invoke() = taskRepository.getAllTasksByPeriodType(TaskPeriodType.ONE_TIME)
}
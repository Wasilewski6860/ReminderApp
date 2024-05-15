package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository

class GetAllOneTimeTasksUseCase(private val taskRepository: TaskRepository) {

    suspend fun execute() = taskRepository.getAllTasksByPeriodType(TaskPeriodType.ONE_TIME)
}
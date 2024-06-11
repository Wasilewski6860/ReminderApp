package com.example.domain.use_case

import com.example.domain.repository.TaskRepository

class GetGroupWithTasksUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(id: Int) = taskRepository.getGroupWithTasks(id)
}
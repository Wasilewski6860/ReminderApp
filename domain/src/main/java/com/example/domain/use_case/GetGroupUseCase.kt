package com.example.domain.use_case

import com.example.domain.repository.TaskRepository

class GetGroupUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(id: Int) = taskRepository.getGroup(id)
}
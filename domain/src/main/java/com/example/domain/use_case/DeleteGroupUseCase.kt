package com.example.domain.use_case

import com.example.domain.repository.TaskRepository

class DeleteGroupUseCase(private val taskRepository: TaskRepository) {

    suspend fun execute(groupId: Int) = taskRepository.deleteGroup(groupId)

}
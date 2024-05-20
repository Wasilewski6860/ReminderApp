package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {

    suspend fun execute(task: Task) = taskRepository.deleteTask(task)
    suspend fun execute(id: Int) = taskRepository.deleteTask(id)
}
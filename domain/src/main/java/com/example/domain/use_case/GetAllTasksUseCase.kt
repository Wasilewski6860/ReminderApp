package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

class GetAllTasksUseCase(private val taskRepository: TaskRepository) {

    suspend fun execute(): MutableList<Task> {
        return taskRepository.getAllTasks()
    }

}
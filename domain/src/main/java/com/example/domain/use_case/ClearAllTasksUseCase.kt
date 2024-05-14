package com.example.domain.use_case

import com.example.domain.repository.TaskRepository

class ClearAllTasksUseCase(private val taskRepository: TaskRepository) {

    fun execute() {
        taskRepository.deleteAllCurrentTasks()
    }

}
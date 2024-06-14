package com.example.domain.use_case

import com.example.domain.repository.TaskRepository

class GetAllTasksUseCase(private val taskRepository: TaskRepository) {

    fun execute() = taskRepository.getAllTasks()

}
package com.example.domain.use_case

import com.example.domain.repository.TaskRepository

class GetTaskUseCase(private val taskRepository: TaskRepository) {

    fun execute() {
        taskRepository.getTask()
    }

}
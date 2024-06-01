package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

class EditTaskUseCase(private val taskRepository: TaskRepository) {

    suspend fun execute(task: Task) = taskRepository.editTask(task)

}
package com.example.domain.use_case.task

import com.example.domain.model.Task
import com.example.domain.repository.ITaskRepository

class SaveTaskUseCase(private val taskRepository: ITaskRepository) {
    suspend operator fun invoke(task: Task) = taskRepository.addTask(task)
}
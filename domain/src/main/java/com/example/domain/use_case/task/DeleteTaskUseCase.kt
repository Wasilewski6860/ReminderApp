package com.example.domain.use_case.task

import com.example.domain.model.Task
import com.example.domain.repository.ITaskRepository

class DeleteTaskUseCase(private val taskRepository: ITaskRepository) {
    suspend operator fun invoke(id: Int) = taskRepository.deleteTask(id)
    suspend operator fun invoke(task: Task) = taskRepository.deleteTask(task)
}
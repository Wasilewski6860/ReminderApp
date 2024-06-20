package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias SaveTaskBaseUseCase = BaseUseCase<Task, Unit>

class SaveTaskUseCase(private val taskRepository: TaskRepository) : SaveTaskBaseUseCase {
    override suspend fun invoke(task: Task)  = taskRepository.addTask(task)
}
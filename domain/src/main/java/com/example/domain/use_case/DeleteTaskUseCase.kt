package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

typealias DeleteTaskBaseUseCase = BaseUseCase<Int, Unit>

class DeleteTaskUseCase(private val taskRepository: TaskRepository) : DeleteTaskBaseUseCase {
    override suspend operator fun invoke(id: Int) = taskRepository.deleteTask(id)
    suspend fun execute(task: Task) = taskRepository.deleteTask(task)
}

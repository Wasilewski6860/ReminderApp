package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

typealias EditTaskBaseUseCase = BaseUseCase<Task, Unit>

class EditTaskUseCase(private val taskRepository: TaskRepository) : EditTaskBaseUseCase {
    override suspend fun invoke(task: Task)  = taskRepository.editTask(task)

}

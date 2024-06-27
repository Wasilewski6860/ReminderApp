package com.example.domain.use_case.task

import com.example.domain.repository.ITaskRepository

class GetTaskUseCase(private val taskRepository: ITaskRepository) {
     operator fun invoke(id: Int) = taskRepository.getTask(id)
}
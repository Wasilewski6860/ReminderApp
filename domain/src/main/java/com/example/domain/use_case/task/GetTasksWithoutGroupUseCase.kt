package com.example.domain.use_case.task

import com.example.domain.repository.ITaskRepository

class GetTasksWithoutGroupUseCase(private val taskRepository: ITaskRepository) {
    operator fun invoke() = taskRepository.getTasksWithoutGroup()
}
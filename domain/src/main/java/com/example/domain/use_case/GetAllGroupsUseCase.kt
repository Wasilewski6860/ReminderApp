package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

class GetAllGroupsUseCase(private val taskRepository: TaskRepository) {

    fun execute() = taskRepository.getAllGroups()

}
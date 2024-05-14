package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

class SaveTaskUseCase(private val taskRepository: TaskRepository) {
    
    fun execute(task: Task) {
        taskRepository.saveTask(task = task)
    }
    
}
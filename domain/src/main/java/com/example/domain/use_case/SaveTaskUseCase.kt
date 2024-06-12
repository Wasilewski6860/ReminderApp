package com.example.domain.use_case

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class SaveTaskUseCase(private val taskRepository: TaskRepository) {
    
    suspend fun execute(task: Task): Flow<Long> {
        return taskRepository.addTask(task)
    }
    
}
package com.example.domain.repository

import com.example.domain.model.Task

interface TaskRepository {

    fun saveTask(task: Task)

    suspend fun getAllTasks(): MutableList<Task>

}
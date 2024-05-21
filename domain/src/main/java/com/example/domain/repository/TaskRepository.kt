package com.example.domain.repository

import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType

interface TaskRepository {

    suspend fun addTask(task: Task)

    suspend fun editTask(task: Task)

    suspend fun deleteTask(task: Task)
    suspend fun deleteTask(id: Int)

    suspend fun deleteAll()

    suspend fun getTask(id: Int): Task

    suspend fun getAllTasksByPeriodType(period: TaskPeriodType): List<Task>

    suspend fun getAllTasks(): List<Task>

}
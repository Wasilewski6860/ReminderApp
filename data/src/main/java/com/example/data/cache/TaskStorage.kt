package com.example.data.cache

import com.example.domain.model.Task

interface TaskStorage {

    suspend fun addTask(task: Task)

    suspend fun editTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun getAllTasks(): List<Task>

    suspend fun getAllTasksByPeriodType(period: String): List<Task>

    suspend fun getTask(id: Int): Task

    suspend fun clearAll()

}
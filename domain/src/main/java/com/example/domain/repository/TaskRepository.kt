package com.example.domain.repository

import com.example.domain.model.Task

interface TaskRepository {

    fun saveTask(task: Task)

    suspend fun getAllTasks(): MutableList<Task>

    fun getTask(/*some params here i guess*/): Task

    fun deleteTask(task: Task)

    fun deleteAllCurrentTasks()

}
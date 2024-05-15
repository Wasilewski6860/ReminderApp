package com.example.data.repositories

import com.example.data.cache.TaskStorage
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository

class TaskRepositoryImpl(private val taskStorage: TaskStorage) : TaskRepository {

    override suspend fun addTask(task: Task) = taskStorage.addTask(task)

    override suspend fun editTask(task: Task) = taskStorage.editTask(task)

    override suspend fun deleteTask(task: Task) = taskStorage.deleteTask(task)

    override suspend fun deleteAll() = taskStorage.clearAll()

    override suspend fun getTask(id: Int): Task = taskStorage.getTask(id)

    override suspend fun getAllTasksByPeriodType(period: TaskPeriodType): List<Task> =
        taskStorage.getAllTasksByPeriodType(period.name)

    override suspend fun getAllTasks(): List<Task> = taskStorage.getAllTasks()

}
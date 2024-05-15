package com.example.data.cache.impl

import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDatabase
import com.example.data.cache.TaskStorage
import com.example.domain.model.Task

class TaskStorageImpl(val mapper: TaskCacheMapper, taskDatabase: TaskDatabase): TaskStorage {

    private val taskDao = taskDatabase.dao

    override suspend fun addTask(task: Task) = taskDao.addTask(mapper.mapToEntity(task))

    override suspend fun editTask(task: Task) = taskDao.editTask(mapper.mapToEntity(task))

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(mapper.mapToEntity(task))

    override suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks().map {taskEntity ->
            mapper.mapFromEntity(taskEntity)
        }
    }

    override suspend fun getAllTasksByPeriodType(period: String): List<Task> {
        return taskDao.getAllTasksByPeriodType(period).map {taskEntity ->
            mapper.mapFromEntity(taskEntity)
        }
    }

    override suspend fun getTask(id: Int): Task = mapper.mapFromEntity(taskDao.getTask(id))

    override suspend fun clearAll() = taskDao.clearAll()

}
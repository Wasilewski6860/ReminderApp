package com.example.data.cache.impl

import com.example.data.cache.GroupCacheMapper
import com.example.data.cache.GroupWithTasksCacheMapper
import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDatabase
import com.example.data.cache.TaskStorage
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task

class TaskStorageImpl(
    val taskCacheMapper: TaskCacheMapper,
    val groupCacheMapper: GroupCacheMapper,
    val groupWithTasksCacheMapper: GroupWithTasksCacheMapper,
    taskDatabase: TaskDatabase
) : TaskStorage {

    private val taskDao = taskDatabase.dao

    override suspend fun addTask(task: Task) = taskDao.addTask(taskCacheMapper.mapToEntity(task))

    override suspend fun editTask(task: Task) = taskDao.editTask(taskCacheMapper.mapToEntity(task))

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(taskCacheMapper.mapToEntity(task))

    override suspend fun deleteTask(id: Int) {
        taskDao.deleteById(id)
    }

    override suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks().map { taskEntity ->
            taskCacheMapper.mapFromEntity(taskEntity)
        }
    }

    override suspend fun getAllTasksByPeriodType(period: String): List<Task> {
        return taskDao.getAllTasksByPeriodType(period).map { taskEntity ->
            taskCacheMapper.mapFromEntity(taskEntity)
        }
    }

    override suspend fun getTask(id: Int): Task = taskCacheMapper.mapFromEntity(taskDao.getTask(id))
    override suspend fun getAllGroups(): List<Group> {
        return taskDao.getAllGroups().map { groupEntity ->
            groupCacheMapper.mapFromEntity(groupEntity)
        }
    }

    override suspend fun getGroup(id: Int): Group = groupCacheMapper.mapFromEntity(taskDao.getGroup(id))

    override suspend fun getGroupWithTasks(id: Int): GroupWithTasks = groupWithTasksCacheMapper.mapFromEntity(taskDao.getGroupWithTasks(id))

    override suspend fun clearAll() = taskDao.clearAll()

}
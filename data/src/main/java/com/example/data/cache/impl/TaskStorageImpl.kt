
package com.example.data.cache.impl

import com.example.data.cache.GroupCacheMapper
import com.example.data.cache.GroupWithTasksCacheMapper
import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDatabase
import com.example.data.cache.TaskStorage
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TaskStorageImpl(
    val taskCacheMapper: TaskCacheMapper,
    val groupCacheMapper: GroupCacheMapper,
    val groupWithTasksCacheMapper: GroupWithTasksCacheMapper,
    taskDatabase: TaskDatabase
) : TaskStorage {

    private val taskDao = taskDatabase.dao

    override suspend fun addTask(task: Task) = flow {
        emit(taskDao.addTask(taskCacheMapper.mapToEntity(task)))
    }

    override suspend fun editTask(task: Task) = taskDao.editTask(taskCacheMapper.mapToEntity(task))

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(taskCacheMapper.mapToEntity(task))

    override suspend fun deleteTask(id: Int) {
        taskDao.deleteById(id)
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { list ->
            list.map {  taskEntity ->
                taskCacheMapper.mapFromEntity(taskEntity)
            }
        }
    }

    override fun getAllTasksByPeriodType(period: String): Flow<List<Task>> {
        return taskDao.getAllTasksByPeriodType(period).map { list ->
            list.map {  taskEntity ->
                taskCacheMapper.mapFromEntity(taskEntity)
            }
        }
    }

    override fun getTask(id: Int): Flow<Task> = taskDao.getTask(id).map { taskEntity -> taskCacheMapper.mapFromEntity(taskEntity) }

    override fun getAllGroups(): Flow<List<Group>> {
        return taskDao.getAllGroups().map { groupEntities ->
            groupEntities.map { taskEntity ->  groupCacheMapper.mapFromEntity(taskEntity) }
        }
    }

    override fun getGroup(id: Int): Flow<Group> = taskDao.getGroup(id).map {
        groupCacheMapper.mapFromEntity(it)
    }

    override fun getGroupWithTasks(id: Int): Flow<GroupWithTasks> = taskDao.getGroupWithTasks(id).map {
        groupWithTasksCacheMapper.mapFromEntity(it)
    }

    override suspend fun clearAll() = taskDao.clearAll()

}

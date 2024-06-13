package com.example.data.repositories

import com.example.data.cache.TaskStorage
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(private val taskStorage: TaskStorage) : TaskRepository {

    override suspend fun addTask(task: Task) = taskStorage.addTask(task)

    override suspend fun editTask(task: Task) = taskStorage.editTask(task)

    override suspend fun deleteTask(task: Task) = taskStorage.deleteTask(task)
    override suspend fun deleteTask(id: Int) {
        taskStorage.deleteTask(id)
    }

    override suspend fun deleteAll() = taskStorage.clearAll()

    override fun getTask(id: Int): Flow<Task> = taskStorage.getTask(id)

    override fun getAllTasksByPeriodType(period: TaskPeriodType): Flow<List<Task>> =
        taskStorage.getAllTasksByPeriodType(period.name)

    override fun getAllTasks(): Flow<List<Task>> = taskStorage.getAllTasks()
    override fun getAllGroups(): Flow<List<Group>> = taskStorage.getAllGroups()
    override fun getGroup(id: Int): Flow<Group> = taskStorage.getGroup(id)
    override fun getGroupWithTasks(id: Int): Flow<GroupWithTasks> = taskStorage.getGroupWithTasks(id)

}
package com.example.data.cache

import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskStorage {

    suspend fun addTask(task: Task): Flow<Long>

    suspend fun editTask(task: Task)

    suspend fun deleteTask(task: Task)
    suspend fun deleteTask(id: Int)

    suspend fun getAllTasks(): List<Task>

    suspend fun getAllTasksByPeriodType(period: String): List<Task>

    suspend fun getTask(id: Int): Task

    suspend fun getAllGroups(): Flow<List<Group>>
    suspend fun getGroup(id: Int): Flow<Group>
    suspend fun getGroupWithTasks(id: Int): Flow<GroupWithTasks>


    suspend fun clearAll()

}
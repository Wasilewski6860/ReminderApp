package com.example.domain.repository

import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType

interface TaskRepository {

    suspend fun addTask(task: Task): Long

    suspend fun editTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun deleteTask(id: Int)

    suspend fun deleteAll()

    suspend fun getTask(id: Int): Task

    suspend fun getAllTasksByPeriodType(period: TaskPeriodType): List<Task>

    suspend fun getAllTasks(): List<Task>

    suspend fun getAllGroups(): Flow<List<Group>>

    suspend fun getGroup(id: Int): Flow<Group>

    suspend fun getGroupWithTasks(id: Int): Flow<GroupWithTasks>

}
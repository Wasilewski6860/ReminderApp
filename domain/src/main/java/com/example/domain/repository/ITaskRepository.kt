package com.example.domain.repository

import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {

    suspend fun addTask(task: Task): Flow<Task>

    suspend fun editTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun deleteTask(id: Int)

    suspend fun deleteAll()

    fun getTask(id: Int): Flow<Task>

    fun getAllTasksByPeriodType(period: TaskPeriodType): Flow<List<Task>>

    fun getAllTasks(): Flow<List<Task>>

    fun getNoTimeTasks(): Flow<List<Task>>

    fun getCountOfNoTimeTasks(): Flow<Int>

    fun getAllTasksCount(): Flow<Int>

    fun getTasksForToday(): Flow<List<Task>>

    fun getTasksForTodayCount(): Flow<Int>

    fun getTasksPlanned(): Flow<List<Task>>

    fun getTasksPlannedCount(): Flow<Int>

    fun getTasksWithFlag(): Flow<List<Task>>

    fun getTasksWithFlagCount(): Flow<Int>



}
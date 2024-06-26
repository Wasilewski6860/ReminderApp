package com.example.domain.repository

import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

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

    suspend fun addGroup(group: Group): Long

    suspend fun editGroup(group: Group)

    fun getAllGroups(): Flow<List<Group>>

    fun getGroup(id: Int): Flow<Group>

    fun getGroupWithTasks(id: Int): Flow<GroupWithTasks>

    suspend fun deleteGroup(groupId: Int)

}
package com.example.data.cache

import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskStorage {

    fun addTask(task: Task): Flow<Task>

    suspend fun editTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun deleteTask(id: Int)

    fun getAllTasks(): Flow<List<Task>>

    fun getAllTasksByPeriodType(period: String): Flow<List<Task>>

    suspend fun addGroup(group: Group)

    fun getTask(id: Int): Flow<Task>

    fun getAllGroups(): Flow<List<Group>>

    fun getGroup(id: Int): Flow<Group>

    fun getGroupWithTasks(id: Int): Flow<GroupWithTasks>
    fun getCountOfTasksInGroup(id: Int): Flow<Int>

    suspend fun clearAll()

    suspend fun deleteGroup(groupId: Int)

}

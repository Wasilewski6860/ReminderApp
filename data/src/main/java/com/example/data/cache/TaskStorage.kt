package com.example.data.cache

import androidx.room.Query
import com.example.data.cache.entity.TaskEntity
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

    fun getNoTimeTasks(): Flow<List<Task>>

    fun getCountOfNoTimeTasks(): Flow<Int>

    fun getAllTasksByPeriodType(period: String): Flow<List<Task>>

    suspend fun addGroup(group: Group)
    suspend fun editGroup(group: Group)

    fun getTask(id: Int): Flow<Task>

    fun getAllGroups(): Flow<List<Group>>

    fun getGroup(id: Int): Flow<Group>

    fun getGroupWithTasks(id: Int): Flow<GroupWithTasks>
    fun getCountOfTasksInGroup(id: Int): Flow<Int>

    suspend fun clearAll()

    suspend fun deleteGroup(groupId: Int)

}

package com.example.domain.repository

import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import kotlinx.coroutines.flow.Flow

interface IGroupRepository {

    suspend fun addGroup(group: Group): Long

    suspend fun editGroup(group: Group)

    fun getAllGroups(): Flow<List<Group>>

    fun getGroup(id: Int): Flow<Group>

    fun getGroupWithTasks(id: Int): Flow<GroupWithTasks>

    suspend fun deleteGroup(groupId: Int)
}
package com.example.data.repositories

import com.example.data.cache.TaskStorage
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GroupRepositoryImpl(
    private val taskStorage: TaskStorage,
): IGroupRepository {
    override suspend fun addGroup(group: Group) = taskStorage.addGroup(group)
    override suspend fun editGroup(group: Group) = taskStorage.editGroup(group)
    override fun getAllGroups(): Flow<List<Group>> = taskStorage.getAllGroups()
    override fun getGroup(id: Int): Flow<Group> = taskStorage.getGroup(id)
    override fun getGroupWithTasks(id: Int): Flow<GroupWithTasks> = taskStorage.getGroupWithTasks(id)
    override suspend fun deleteGroup(groupId: Int) = taskStorage.deleteGroup(groupId)
}
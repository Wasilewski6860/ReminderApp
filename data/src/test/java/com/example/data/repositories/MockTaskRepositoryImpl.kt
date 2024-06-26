package com.example.data.repositories

import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockTaskRepositoryImpl : TaskRepository {

    private val _testTaskData = Task(
        id = 0,
        name = "name",
        description = "desc",
        reminderTime = 1L,
        reminderCreationTime = 1L,
        reminderTimePeriod = 1L,
        type = TaskPeriodType.NO_TIME,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = 0,
        color = 0
    )
    val testTaskData get() = _testTaskData

    override suspend fun addTask(task: Task): Flow<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun editTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun getTask(id: Int): Flow<Task> {
        return flowOf(testTaskData)
    }

    override fun getAllTasksByPeriodType(period: TaskPeriodType): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getNoTimeTasks(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getCountOfNoTimeTasks(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun getAllTasksCount(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun getTasksForToday(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getTasksForTodayCount(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun getTasksPlanned(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getTasksPlannedCount(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun getTasksWithFlag(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getTasksWithFlagCount(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun addGroup(group: Group): Long {
        TODO("Not yet implemented")
    }

    override suspend fun editGroup(group: Group) {
        TODO("Not yet implemented")
    }

    override fun getAllGroups(): Flow<List<Group>> {
        TODO("Not yet implemented")
    }

    override fun getGroup(id: Int): Flow<Group> {
        TODO("Not yet implemented")
    }

    override fun getGroupWithTasks(id: Int): Flow<GroupWithTasks> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGroup(groupId: Int) {
        TODO("Not yet implemented")
    }

}
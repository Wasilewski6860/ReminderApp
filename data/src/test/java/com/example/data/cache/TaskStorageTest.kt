package com.example.data.cache

import com.example.data.TestData
import com.example.data.base.BaseDataTest
import com.example.data.cache.entity.TaskEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.test.inject

class TaskStorageTest: BaseDataTest() {

    private val taskStorage: TaskStorage by inject()
    private val taskDao: TaskDao by inject()

    @Test
    fun test_addTask() = runBlocking {
        val task = TestData.firstTask
        val taskEntity = TestData.firstTaskEntity

        coEvery { taskDao.addTask(any()) } returns 1L
        coEvery { taskDao.getTask(1) } returns taskEntity

        val result = taskStorage.addTask(task)

        result.collect { addedTask ->
            assert(addedTask.id == task.id)
            assert(addedTask.name == task.name)
        }

        coVerify { taskDao.addTask(any()) }
    }

    @Test
    fun test_editTask() = runBlocking {
        val updatedTask = TestData.firstTaskEdited
        val updatedTaskEntity = TestData.firstTaskEntityEdited
        val slotTaskEntity = slot<TaskEntity>()

        coEvery { taskDao.editTask(capture(slotTaskEntity)) } answers {}

        taskStorage.editTask(updatedTask)

        coVerify { taskDao.editTask(updatedTaskEntity) }
        val capturedTaskEntity = slotTaskEntity.captured

        //TODO вынести сравнение куда-нибудь
        assertEquals(updatedTask.id, capturedTaskEntity.id)
        assertEquals(updatedTask.name, capturedTaskEntity.name)
        assertEquals(updatedTask.type.toString(), capturedTaskEntity.periodicType)

        coEvery { taskDao.getTask(updatedTask.id) } returns capturedTaskEntity

        val editedTask = taskStorage.getTask(updatedTask.id).first()

        assertEquals(updatedTask.id, editedTask.id)
        assertEquals(updatedTask.name, editedTask.name)
    }

    @Test
    fun test_deleteTask() = runBlocking {
        val task = TestData.firstTask
        val taskEntity = TestData.firstTaskEntity
        coEvery { taskDao.deleteTask(any()) } returns Unit
        taskStorage.deleteTask(task)
        coVerify { taskDao.deleteTask(taskEntity) }
    }

    @Test
    fun test_getAllTasks() = runBlocking {
        val tasks = TestData.tasks
        every { taskDao.getAllTasks() } returns flowOf(TestData.taskEntities)
        val result = taskStorage.getAllTasks().first()
        assertEquals(result, tasks)
    }

    @Test
    fun test_getNoTimeTasks() = runBlocking {
        val tasks = TestData.noTimeTasks
        every { taskDao.getNoTimeTasks() } returns flowOf(TestData.noTimeTaskEntities)
        val result = taskStorage.getNoTimeTasks().first()
        assertEquals(result, tasks)
    }

    @Test
    fun test_getTask() = runBlocking {
        val taskId = 1
        val task = TestData.firstTask
        coEvery { taskDao.getTask(taskId) } returns TestData.firstTaskEntity

        val result = taskStorage.getTask(taskId).first()

        assertEquals(task, result)
    }

    @Test
    fun test_getAllGroups() = runBlocking {
//        val groups = TestData.groups
//        every { taskDao.getAllGroups() } returns flowOf(TestData.groupEntities)
//        groups.forEach {
//            coEvery  { taskDao.getCountOfTasksInGroup(it.groupId) } returns it.tasksCount
//        }
//        val result = taskStorage.getAllGroups().first()
//        assertEquals(groups, result)
    }

    @Test
    fun test_getGroup() = runBlocking {
//        val groupId = 1
//        val group = TestData.firstGroup
//        every { taskDao.getGroup(groupId) } returns flowOf(TestData.firstGroupEntity)
//        coEvery  { taskDao.getCountOfTasksInGroup(groupId) } returns TestData.tasks.count { it.groupId == groupId }
//        val result = taskStorage.getGroup(groupId).first()
//        assertEquals(group, result)
    }

    @Test
    fun test_getGroupWithTasks() = runBlocking {
//        val groupId = 1
//        val groupWithTask = TestData.firstGroupWithTasks
//        every { taskDao.getGroupWithTasks(groupId) } returns flowOf(TestData.firstGroupWithTasksEntity)
//        val result = taskStorage.getGroupWithTasks(groupId).first()
//        assertEquals(groupWithTask, result)
    }

    @Test
    fun test_deleteGroup() = runBlocking {
        val groupId = 1
        coEvery { taskDao.deleteGroup(groupId) } returns Unit
        taskStorage.deleteGroup(groupId)
        coVerify { taskDao.deleteGroup(groupId) }
    }

    @Test
    fun test_clearAll() = runBlocking {
        coEvery { taskDao.clearAllTasks() } returns Unit
        taskStorage.clearAll()
        coVerify { taskDao.clearAllTasks() }
    }
}
package com.example.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.cache.TaskDao
import com.example.data.cache.TaskDatabase
import com.example.domain.model.TaskPeriodType
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var taskDao: TaskDao
    private lateinit var database: TaskDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), TaskDatabase::class.java).allowMainThreadQueries().build()
        taskDao = database.getTaskDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun testOfInsertAndGetTask() = runBlocking {
        val taskEntity = TestData.firstTaskEntity
        taskDao.addGroup(TestData.firstGroupEntity)
        taskDao.addTask(taskEntity)
        val retrievedTask = taskDao.getTask(1).first()
        assertEquals(taskEntity,retrievedTask)
    }

    @Test
    fun testOfDeletionTask() = runBlocking {
        val groupEntity = TestData.firstGroupEntity
        taskDao.addGroup(groupEntity)
        val taskEntity = TestData.firstTaskEntity
        taskDao.addTask(taskEntity)
        taskDao.deleteTask(taskEntity)
        val tasks = taskDao.getAllTasks().first()
        assertFalse(tasks.contains(taskEntity))
    }

    @Test
    fun testOfGettingAllTasks() = runBlocking {
        taskDao.addGroup(TestData.firstGroupEntity)
        taskDao.addGroup(TestData.secondGroupEntity)
        taskDao.addTask(TestData.firstTaskEntity)
        taskDao.addTask(TestData.secondTaskEntity)
        taskDao.addTask(TestData.thirdTaskEntity)
        val tasks = taskDao.getAllTasks().first()
        assertEquals(tasks, TestData.taskEntities)
    }

    @Test
    fun getNoTimeTasks() = runBlocking {
        taskDao.addGroup(TestData.firstGroupEntity)
        taskDao.addGroup(TestData.secondGroupEntity)
        taskDao.addGroup(TestData.thirdGroupEntity)
        taskDao.addTask(TestData.firstTaskEntity)
        taskDao.addTask(TestData.secondTaskEntity)
        taskDao.addTask(TestData.thirdTaskEntity)
        val noTimeTasks = taskDao.getNoTimeTasks().first()
        assertEquals(noTimeTasks, TestData.noTimeTaskEntities)
    }

    @Test
    fun getAllTasksByPeriodTime() = runBlocking {
        taskDao.addGroup(TestData.firstGroupEntity)
        taskDao.addGroup(TestData.secondGroupEntity)
        taskDao.addGroup(TestData.thirdGroupEntity)
        taskDao.addTask(TestData.firstTaskEntity)
        taskDao.addTask(TestData.secondTaskEntity)
        taskDao.addTask(TestData.thirdTaskEntity)
        val periodicTasks = taskDao.getAllTasksByPeriodType(TaskPeriodType.PERIODIC.toString()).first()
        val oneTimeTasks = taskDao.getAllTasksByPeriodType(TaskPeriodType.ONE_TIME.toString()).first()
        val noTimeTasks = taskDao.getAllTasksByPeriodType(TaskPeriodType.NO_TIME.toString()).first()
        assertEquals(periodicTasks, TestData.periodicTaskEntities)
        assertEquals(oneTimeTasks, TestData.oneTimeTaskEntities)
        assertEquals(noTimeTasks, TestData.noTimeTaskEntities)
    }

    @Test
    fun insertAndGetGroup() = runBlocking {
        val groupEntity = TestData.firstGroupEntity
        taskDao.addGroup(groupEntity)
        val retrievedGroup = taskDao.getGroup(groupEntity.groupId).first()
        assertEquals(retrievedGroup,groupEntity)
    }

    @Test
    fun getAllGroups() = runBlocking {
        taskDao.addGroup(TestData.firstGroupEntity)
        taskDao.addGroup(TestData.secondGroupEntity)
        taskDao.addGroup(TestData.thirdGroupEntity)
        val groups = taskDao.getAllGroups().first()
        assertEquals(groups, TestData.groupEntities)
    }

    @Test
    fun getGroupWithTasks() = runBlocking {
        taskDao.addGroup(TestData.firstGroupEntity)
        taskDao.addTask(TestData.firstTaskEntity)
        taskDao.addTask(TestData.thirdTaskEntity)
        val groupWithTasks = taskDao.getGroupWithTasks(TestData.firstGroupEntity.groupId).first()
        assertEquals(groupWithTasks, TestData.firstGroupWithTasksEntity)
    }

    @Test
    fun deleteGroup() = runBlocking {
        taskDao.addGroup(TestData.firstGroupEntity)
        taskDao.addTask(TestData.firstTaskEntity)
        taskDao.addTask(TestData.thirdTaskEntity)

        taskDao.deleteGroup(TestData.firstGroupEntity.groupId)
        val allGroups = taskDao.getAllGroups().first()
        assertTrue(allGroups.isEmpty())

        val allTasks = taskDao.getAllTasks().first()
        assertTrue(allTasks.isEmpty())

        val tasksByGroup = taskDao.getGroupWithTasks(TestData.firstGroupEntity.groupId).first()
        assertTrue(tasksByGroup==null)
    }

    @Test
    fun clearAll() = runBlocking {
        taskDao.addGroup(TestData.firstGroupEntity)
        taskDao.addGroup(TestData.secondGroupEntity)
        taskDao.addTask(TestData.firstTaskEntity)
        taskDao.addTask(TestData.secondTaskEntity)
        taskDao.addTask(TestData.thirdTaskEntity)

        taskDao.clearAllTasks()

        val allTasks = taskDao.getAllTasks().first()
        assertTrue(allTasks.isEmpty())
    }
}

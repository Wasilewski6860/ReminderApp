package com.example.reminderapp.test.base

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.MainActivity
import com.example.reminderapp.di.TestData
import com.example.reminderapp.di.dataTestModule
import com.example.reminderapp.di.domainTestModule
import com.example.reminderapp.di.presentationTestModule
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

abstract class BaseScreenTest : SetupDITest() {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    val taskRepository: ITaskRepository by inject()
    val groupRepository: IGroupRepository by inject()
    val context: Context by inject()

    @Before
    override fun setUp() {
        super.setUp()
        coEvery { taskRepository.getTasksForToday() } returns flowOf(TestData.todayTasks)
        coEvery { taskRepository.getTasksForTodayCount() } returns flowOf(TestData.todayTasks.size)
        coEvery { taskRepository.getTasksPlanned() } returns flowOf(TestData.plannedTasks)
        coEvery { taskRepository.getTasksPlannedCount() } returns flowOf(TestData.plannedTasks.size)
        coEvery { taskRepository.getTasksWithFlagCount() } returns flowOf(TestData.tasksWithFlag.size)
        coEvery { taskRepository.getTasksWithFlag() } returns flowOf(TestData.tasksWithFlag)
        coEvery { taskRepository.getCountOfNoTimeTasks() } returns flowOf(TestData.noTimeTasks.size)
        coEvery { taskRepository.getAllTasksCount() } returns flowOf(TestData.tasks.size)
        coEvery { taskRepository.getAllTasks() } returns flowOf(TestData.tasks)
        coEvery { taskRepository.getNoTimeTasks() } returns flowOf(TestData.noTimeTasks)
        coEvery { groupRepository.getAllGroups() } returns flowOf(TestData.groups)
    }
}
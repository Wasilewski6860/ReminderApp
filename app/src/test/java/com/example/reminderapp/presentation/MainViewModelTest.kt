package com.example.reminderapp.presentation

import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.presentation.base.BaseViewModelTest
import com.example.reminderapp.presentation.main.MainViewModel
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.inject
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.main.MainUiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MainViewModelTest: BaseViewModelTest() {

    private val taskRepository: ITaskRepository by inject()
    private val groupRepository: IGroupRepository by inject()
    private val viewModel: MainViewModel by inject()

    @Test
    fun test_fetch_data_success() = runBlocking {

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
        coEvery { taskRepository.getTasksWithoutGroupCount() } returns flowOf(TestData.tasksWithoutGroup.size)
        coEvery { taskRepository.getTasksWithoutGroup() } returns flowOf(TestData.tasksWithoutGroup)

        viewModel.fetchData(Unit)
        val uiState = viewModel.uiState.first()
        assert(uiState is UiState.Success)
        val successState = uiState as UiState.Success<MainUiState>
        val mainUiState = successState.data

        assertEquals(TestData.groups, mainUiState.groups)
        assertEquals(TestData.todayTasks.size, mainUiState.todayCount)
        assertEquals(TestData.plannedTasks.size, mainUiState.plannedCount)
        assertEquals(TestData.tasksWithFlag.size, mainUiState.withFlagCount)
        assertEquals(TestData.noTimeTasks.size, mainUiState.noTimeCount)
        assertEquals(TestData.tasks.size, mainUiState.allCount)
    }

    @Test
    fun test_fetch_data_error() = runBlocking {

        coEvery { taskRepository.getTasksForToday() } returns flowOf(TestData.todayTasks)
        coEvery { taskRepository.getTasksForTodayCount() } returns flowOf(TestData.todayTasks.size)
        coEvery { taskRepository.getTasksPlanned() } returns flowOf(TestData.plannedTasks)
        coEvery { taskRepository.getTasksPlannedCount() } returns flowOf(TestData.plannedTasks.size)
        coEvery { taskRepository.getTasksWithFlagCount() } returns flow { throw Exception("Error fetching tasks with flag") }
        coEvery { taskRepository.getCountOfNoTimeTasks() } returns flowOf(TestData.noTimeTasks.size)
        coEvery { taskRepository.getAllTasksCount() } returns flowOf(TestData.tasks.size)
        coEvery { groupRepository.getAllGroups() } returns flowOf(TestData.groups)

        viewModel.fetchData(Unit)
        val uiState = viewModel.uiState.first()

        assert(uiState is UiState.Error)
        val errorState = uiState as UiState.Error
        assertEquals("java.lang.Exception: Error fetching tasks with flag", errorState.message)

    }
}
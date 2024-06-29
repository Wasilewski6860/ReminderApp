package com.example.reminderapp.presentation

import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.base.BaseViewModelTest
import com.example.reminderapp.presentation.base.OperationResult
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.mainscreen.MainUiState
import com.example.reminderapp.presentation.navigation.TasksListTypeCase
import com.example.reminderapp.presentation.new_list.NewListViewModel
import com.example.reminderapp.presentation.reminder_list.ReminderListViewModel
import com.example.reminderapp.presentation.reminder_list.RemindersListUiState
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.test.inject
import java.lang.Exception
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class ReminderListViewModelTest: BaseViewModelTest() {

    private val groupRepository: IGroupRepository by inject()
    private val taskRepository: ITaskRepository by inject()
    private val viewModel: ReminderListViewModel by inject()

    fun setSuccessfulMocks() {
        coEvery {groupRepository.getGroupWithTasks(any())} returns flowOf(TestData.groupWithTasks)
        coEvery {taskRepository.getTasksForToday()} returns flowOf(TestData.todayTasks)
        coEvery {taskRepository.getTasksPlanned()} returns flowOf(TestData.plannedTasks)
        coEvery {taskRepository.getNoTimeTasks()} returns flowOf(TestData.noTimeTasks)
        coEvery {taskRepository.getTasksWithFlag()} returns flowOf(TestData.tasksWithFlag)
        coEvery {taskRepository.getAllTasks()} returns flowOf(TestData.tasks)
        coEvery {taskRepository.getTask(any())} returns flowOf(TestData.firstTask)
    }

    fun setErrorMocks() {
        coEvery {taskRepository.getTask(any())} throws Exception("Exception getTask")
        coEvery {taskRepository.deleteTask(task = any())} throws Exception("Exception deleteTask")
        coEvery {taskRepository.deleteTask(id = any())} throws Exception("Exception deleteTask")
    }

    @Test
    fun test_fetch_by_group() = runBlocking {
        setSuccessfulMocks()
        viewModel.fetchData(TasksListTypeCase.GroupTasks(1))
        val uiState = viewModel.uiState.first()
        assert(uiState is UiState.Success)
        val successState = uiState as UiState.Success<RemindersListUiState>
        val mainUiState = successState.data
        assertEquals(TestData.groupWithTasks.tasks, mainUiState.tasks)
    }

    @Test
    fun test_fetch_no_time() = runBlocking {
        setSuccessfulMocks()
        viewModel.fetchData(TasksListTypeCase.TasksNoTime)
        val uiState = viewModel.uiState.first()
        assert(uiState is UiState.Success)
        val successState = uiState as UiState.Success<RemindersListUiState>
        val mainUiState = successState.data
        assertEquals(TestData.noTimeTasks, mainUiState.tasks)
    }

    @Test
    fun test_fetch_all() = runBlocking {
        setSuccessfulMocks()
        viewModel.fetchData(TasksListTypeCase.AllTasks)
        val uiState = viewModel.uiState.first()
        assert(uiState is UiState.Success)
        val successState = uiState as UiState.Success<RemindersListUiState>
        val mainUiState = successState.data
        assertEquals(TestData.tasks, mainUiState.tasks)
    }

    @Test
    fun test_fetch_planned() = runBlocking {
        setSuccessfulMocks()
        viewModel.fetchData(TasksListTypeCase.PlannedTasks)
        val uiState = viewModel.uiState.first()
        assert(uiState is UiState.Success)
        val successState = uiState as UiState.Success<RemindersListUiState>
        val mainUiState = successState.data
        assertEquals(TestData.plannedTasks, mainUiState.tasks)
    }

    @Test
    fun test_fetch_with_flag() = runBlocking {
        setSuccessfulMocks()
        viewModel.fetchData(TasksListTypeCase.TasksWithFlag)
        val uiState = viewModel.uiState.first()
        assert(uiState is UiState.Success)
        val successState = uiState as UiState.Success<RemindersListUiState>
        val mainUiState = successState.data
        assertEquals(TestData.tasksWithFlag, mainUiState.tasks)
    }

    @Test
    fun test_fetch_today() = runBlocking {
        setSuccessfulMocks()
        viewModel.fetchData(TasksListTypeCase.TodayTasks)
        val uiState = viewModel.uiState.first()
        assert(uiState is UiState.Success)
        val successState = uiState as UiState.Success<RemindersListUiState>
        val mainUiState = successState.data
        assertEquals(TestData.todayTasks, mainUiState.tasks)
    }

    @Test
    fun delete_task_success() = runBlocking {
        setSuccessfulMocks()
        viewModel.deleteTask(TestData.firstTask)
        coVerify { taskRepository.deleteTask(task = any()) }
        val result = viewModel.operationResult.first()
        assertTrue(result is OperationResult.Success)
    }

    @Test
    fun delete_task_error() = runBlocking {
        setErrorMocks()
        viewModel.deleteTask(TestData.firstTask)
        val result = viewModel.operationResult.first()
        assertTrue(result is OperationResult.Error)
    }
}
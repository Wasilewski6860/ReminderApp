package com.example.reminderapp.presentation

import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.domain.use_case.reminder.CreateReminderUseCase
import com.example.domain.use_case.reminder.EditReminderUseCase
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.base.BaseViewModelTest
import com.example.reminderapp.presentation.base.OperationResult
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.create_reminder.CreateReminderViewModel
import com.example.reminderapp.presentation.mainscreen.MainUiState
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CreateReminderViewModelTest: BaseViewModelTest() {

    private val taskRepository: ITaskRepository by inject()
    private val viewModel: CreateReminderViewModel by inject()

    @Test
    fun test_save_success() = runBlocking {

        viewModel.onNameTextChanged(TestData.firstTask.name)
        viewModel.onDescriptionTextChanged(TestData.firstTask.description)
        viewModel.onFirstTimeChanged(TestData.firstTask.reminderTime)
        viewModel.onPeriodChanged(TestData.firstTask.reminderTimePeriod)
        viewModel.onFlagChanged(TestData.firstTask.isMarkedWithFlag)
        viewModel.onGroupIdChanged(TestData.firstTask.groupId)

        coEvery { taskRepository.addTask(any()) } returns flowOf(TestData.firstTask)

        viewModel.saveTask()
        coVerify { taskRepository.addTask(any()) }

        // Verify saveResult is success
        val saveResult = viewModel.saveResult.first()
        assertEquals(OperationResult.Success(Unit), saveResult)
    }

    @Test
    fun test_validation_success() = runBlocking {
        viewModel.onNameTextChanged(TestData.firstTask.name)
        viewModel.onDescriptionTextChanged(TestData.firstTask.description)
        viewModel.onFirstTimeChanged(TestData.firstTask.reminderTime)
        viewModel.onPeriodChanged(TestData.firstTask.reminderTimePeriod)
        viewModel.onFlagChanged(TestData.firstTask.isMarkedWithFlag)
        viewModel.onGroupIdChanged(TestData.firstTask.groupId)

        val result = viewModel.validate()

        assertEquals(result, true)
    }

    @Test
    fun test_validation_error() = runBlocking {
        viewModel.onNameTextChanged("")
        viewModel.onDescriptionTextChanged(TestData.firstTask.description)
        viewModel.onFirstTimeChanged(TestData.firstTask.reminderTime)
        viewModel.onPeriodChanged(TestData.firstTask.reminderTimePeriod)
        viewModel.onFlagChanged(TestData.firstTask.isMarkedWithFlag)
        viewModel.onGroupIdChanged(null)

        assertEquals(viewModel.validate(), false)

        viewModel.onNameTextChanged(TestData.firstTask.name)
        assertEquals(viewModel.validate(), false)

        viewModel.onGroupIdChanged(TestData.firstTask.groupId)
        assertEquals(viewModel.validate(), true)
    }
}
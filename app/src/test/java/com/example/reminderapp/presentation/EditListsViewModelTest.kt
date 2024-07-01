package com.example.reminderapp.presentation

import com.example.domain.model.Group
import com.example.domain.repository.IGroupRepository
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.base.BaseViewModelTest
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.editorlistsscreen.EditListsViewModel
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class EditListsViewModelTest: BaseViewModelTest() {

    private val groupRepository: IGroupRepository by inject()
    private val viewModel: EditListsViewModel by inject()

    @Test
    fun test_fetch_data_success() = runBlocking {
        coEvery {groupRepository.getAllGroups()} returns flowOf(TestData.groups)
        viewModel.fetchData()
        val uiState = viewModel.uiState.first()
        assert(uiState is UiState.Success)
        val successState = uiState as UiState.Success<List<Group>>
        val mainUiState = successState.data
        assertEquals(TestData.groups, mainUiState)
    }

    @Test
    fun test_fetch_data_error() = runBlocking {
        coEvery {groupRepository.getAllGroups()} returns flow { throw Exception("Test exception") }
        viewModel.fetchData()
        val uiState = viewModel.uiState.first()
        assert(uiState is UiState.Error)
        val errorState = uiState as UiState.Error
        assertEquals("java.lang.Exception: Test exception", errorState.message)
    }

    @Test
    fun test_deletion_success() = runBlocking {
        coEvery {groupRepository.getGroup(any())} returns flowOf(TestData.firstGroup)
        coEvery {groupRepository.getAllGroups()} returns flowOf(TestData.groups)
        coEvery {groupRepository.getGroupWithTasks(any())} returns flowOf(TestData.groupWithTasks)
        viewModel.deleteGroup(1)
        val uiState = viewModel.uiState.first()
        assertFalse(uiState is UiState.Error)
    }

    @Test
    fun test_deletion_error() = runBlocking {
        coEvery {groupRepository.getGroupWithTasks(any())} returns flow { throw Exception("Test deletion exception") }
        coEvery {groupRepository.getAllGroups()} returns flowOf(TestData.groups)
        viewModel.deleteGroup(1)
        val uiState = viewModel.uiState.first()
        assertTrue(uiState is UiState.Error)
        val errorState = uiState as UiState.Error
        assertEquals("java.lang.Exception: Test deletion exception", errorState.message)
    }
}
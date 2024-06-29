package com.example.reminderapp.presentation

import com.example.domain.repository.IGroupRepository
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.base.BaseViewModelTest
import com.example.reminderapp.presentation.base.OperationResult
import com.example.reminderapp.presentation.new_list.NewListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.inject
import java.lang.Exception
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class NewListViewModelTest: BaseViewModelTest() {

    private val groupRepository: IGroupRepository by inject()
    private val viewModel: NewListViewModel by inject()

    @Test
    fun test_save_success() = runBlocking {

        viewModel.onGroupNameChanged(TestData.firstGroup.groupName)
        viewModel.onGroupImageChanged(TestData.firstGroup.groupImage)
        viewModel.onGroupColorChanged(TestData.firstGroup.groupColor)

        coEvery {groupRepository.addGroup(any())} returns TestData.firstGroup.groupId.toLong()
        viewModel.saveList()
        coVerify { groupRepository.addGroup(any()) }

        val saveResult = viewModel.saveResult.first()
        assertEquals(OperationResult.Success(TestData.firstGroup.groupId.toLong()), saveResult)
    }

    @Test
    fun test_save_error() = runBlocking {

        viewModel.onGroupNameChanged(TestData.firstGroup.groupName)
        viewModel.onGroupImageChanged(TestData.firstGroup.groupImage)
        viewModel.onGroupColorChanged(TestData.firstGroup.groupColor)

        coEvery {groupRepository.addGroup(any())} throws Exception("Saving exception")
        viewModel.saveList()
        coVerify { groupRepository.addGroup(any()) }
        val uiState = viewModel.saveResult.first()

        assert(uiState is OperationResult.Error)
        val errorState = uiState as OperationResult.Error
        assertEquals("java.lang.Exception: Saving exception", errorState.message)
    }

    @Test
    fun test_validation() = runBlocking {
        viewModel.onGroupNameChanged("")
        viewModel.onGroupImageChanged(TestData.firstGroup.groupImage)
        viewModel.onGroupColorChanged(TestData.firstGroup.groupColor)

        assertEquals(viewModel.validate(), false)

        viewModel.onGroupNameChanged(TestData.firstGroup.groupName)
        assertEquals(viewModel.validate(), true)
    }
}
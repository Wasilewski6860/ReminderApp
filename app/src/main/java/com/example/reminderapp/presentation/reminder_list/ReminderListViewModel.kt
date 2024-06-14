package com.example.reminderapp.presentation.reminder_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.use_case.GetAllGroupsUseCase
import com.example.domain.use_case.GetGroupWithTasksUseCase
import com.example.domain.use_case.SaveTaskUseCase
import com.example.reminderapp.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ReminderListViewModel(
    val getGroupWithTasksUseCase: GetGroupWithTasksUseCase
) : ViewModel() {
    // TODO: Implement the ViewModel

    private val _uiState = MutableStateFlow<UiState<GroupWithTasks>>(UiState.Loading)
    val uiState: StateFlow<UiState<GroupWithTasks>> = _uiState

    fun fetchGroupWithTasks(id: Int) {
        viewModelScope.launch {
            getGroupWithTasksUseCase.execute(id)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

}
package com.example.reminderapp.presentation.create_reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.use_case.GetAllGroupsUseCase
import com.example.domain.use_case.SaveTaskUseCase
import com.example.reminderapp.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CreateReminderViewModel(
    val createTaskUseCase: SaveTaskUseCase,
    val getAllGroupsUseCase: GetAllGroupsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Group>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Group>>> = _uiState

    private val _saveResult = MutableStateFlow<Long>(-1)
    val saveResult: StateFlow<Long> = _saveResult

    fun fetchGroups() {
        viewModelScope.launch {
            getAllGroupsUseCase(Unit)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun saveTask(
       task: Task
    ) {
        viewModelScope.launch {
            createTaskUseCase(
                Task(
                    id = 0,
                    name = task.name,
                    description = task.description,
                    reminderCreationTime = task.reminderCreationTime,
                    reminderTime = task.reminderTime,
                    reminderTimePeriod = task.reminderTimePeriod,
                    type = task.type,
                    isActive = task.isActive,
                    isMarkedWithFlag = task.isMarkedWithFlag,
                    groupId = task.groupId,
                    color = task.color
                )
            ).catch { e ->
                _uiState.value = UiState.Error(e.toString())
            }.collect {
                _saveResult.value = it
            }
        }
    }

}
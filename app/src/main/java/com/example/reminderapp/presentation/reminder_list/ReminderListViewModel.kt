package com.example.reminderapp.presentation.reminder_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.use_case.GetAllTasksUseCase
import com.example.domain.use_case.GetGroupWithTasksUseCase
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.navigation.TasksListTypeCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ReminderListViewModel(
    private val getGroupWithTasksUseCase: GetGroupWithTasksUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {
    // TODO: Implement the ViewModel

    private val _uiState = MutableStateFlow<UiState<GroupWithTasks>>(UiState.Loading)
    val uiState: StateFlow<UiState<GroupWithTasks>> = _uiState

    private val tasksListFlowData = MutableStateFlow<List<Task>>(emptyList())
    val tasksList get() = tasksListFlowData

    fun fetchGroupWithTasks(id: Int) {
        viewModelScope.launch {
            getGroupWithTasksUseCase(id)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun getNeededTaskList(taskListType: TasksListTypeCase) {
        viewModelScope.launch {
            getAllTasksUseCase(Unit)
                .catch { e ->
                    Log.e("FETCHING DATA FROM DATABASE PROCESS", e.toString())
                }
                .collect {
                    tasksListFlowData.value = getTasksList(taskListType, it)
                }
        }
    }

    private fun getTasksList(case: TasksListTypeCase, data: List<Task>): List<Task> {
        var neededList = mutableListOf<Task>()
        when (case) {
            TasksListTypeCase.TodayTasks -> { /** Use method which will return today tasks list **/ }
            TasksListTypeCase.PlannedTasks -> {
                data.forEach {
                    if (it.type == TaskPeriodType.ONE_TIME) neededList.add(it)
                }
            }
            TasksListTypeCase.AllTasks -> neededList = data.toMutableList()
            TasksListTypeCase.TasksWithFlag -> {
                data.forEach {
                    if (it.isMarkedWithFlag) neededList.add(it)
                }
            }
        }
        return neededList
    }

}
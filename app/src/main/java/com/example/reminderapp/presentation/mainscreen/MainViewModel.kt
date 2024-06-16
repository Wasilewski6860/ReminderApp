package com.example.reminderapp.presentation.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.use_case.GetAllGroupsUseCase
import com.example.domain.use_case.GetAllTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {

    private val groupsListFlowData = MutableStateFlow<List<Group>>(emptyList())
    val groupsListData get() = groupsListFlowData

    private val tasksListFlowData = MutableStateFlow<List<Task>>(emptyList())

    val todayReminders get() = "0" // TODO replace this temp code with normal method
    val plannedReminders get() = countOneTimeReminders().toString()
    val allReminders get() = groupsListData.value.size.toString()
    val remindersWithFlag get() = countTasksWithFlag().toString()

    fun fetchTaskGroups() {
        viewModelScope.launch {
            getAllGroupsUseCase(Unit)
                .catch { e ->
                    Log.e("FETCHING DATA FROM DATABASE", e.toString())
                }
                .collect {
                    groupsListFlowData.value = it
                }
        }
    }

    fun getAllTasks() {
        viewModelScope.launch {
            getAllTasksUseCase(Unit)
                .catch { e ->
                    Log.e("FETCHING DATA FROM DATABASE", e.toString())
                }
                .collect {
                    tasksListFlowData.value = it
                }
        }
    }

    private fun countOneTimeReminders(): Int {
        var counter = 0
        viewModelScope.launch {
            try {
                tasksListFlowData.value.forEach { reminder ->
                    if (reminder.type == TaskPeriodType.ONE_TIME) counter++
                }
            } catch (e: Exception) {
                Log.e("COUNTING ONE-TIME TASKS PROCESS", e.toString())
            }
        }
        return counter
    }

    private fun countTasksWithFlag(): Int {
        var counter = 0
        viewModelScope.launch {
            try {
                tasksListFlowData.value.forEach { reminder ->
                    if (reminder.isMarkedWithFlag) counter++
                }
            } catch (e: Exception) {
                Log.e("COUNTING TASKS WITH FLAG PROCESS", e.toString())
            }
        }
        return counter
    }

}
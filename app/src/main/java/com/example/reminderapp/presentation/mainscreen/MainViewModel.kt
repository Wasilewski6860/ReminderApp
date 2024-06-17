package com.example.reminderapp.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.GetAllGroupsUseCase
import com.example.domain.use_case.GetTasksForTodayCountUseCase
import com.example.domain.use_case.GetTasksPlannedCountUseCase
import com.example.domain.use_case.GetTasksWithFlagCountUseCase
import com.example.reminderapp.presentation.TestData
import com.example.reminderapp.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val groups: List<Group>,
    val todayCount: Int,
    val plannedCount: Int,
    val withFlagCount: Int,
    val allCount: Int,
)

class MainViewModel(
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val getTasksForTodayCountUseCase: GetTasksForTodayCountUseCase,
    private val getTasksPlannedCountUseCase: GetTasksPlannedCountUseCase,
    private val getTasksWithFlagCountUseCase: GetTasksWithFlagCountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MainUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<MainUiState>> = _uiState

    fun fetchData() {
        viewModelScope.launch {
            _uiState.value = UiState.Success(
                MainUiState(
                     TestData().getTestList(), 6, 5, 1, 2
                )
            )
//            TODO Делать так:
//            combine(
//                getAllGroupsUseCase(Unit),
//                getTasksForTodayCountUseCase(Unit),
//                getTasksPlannedCountUseCase(Unit),
//                getTasksWithFlagCountUseCase(Unit),
//            ) { allGroups, todayCount, plannedCount, withFlagCount  ->
//                MainUiState(
//                    allGroups, todayCount, plannedCount, withFlagCount, allGroups.size
//                )
//            }.catch { e ->
//                _uiState.value = UiState.Error(e.toString())
//            }.collect {
//                _uiState.value = UiState.Success(it)
//            }


        }
    }



}

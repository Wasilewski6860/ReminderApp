package com.example.reminderapp.presentation.mainscreen

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.GetAllGroupsUseCase
import com.example.domain.use_case.GetAllTasksCountUseCase
import com.example.domain.use_case.GetTasksForTodayCountUseCase
import com.example.domain.use_case.GetTasksPlannedCountUseCase
import com.example.domain.use_case.GetTasksWithFlagCountUseCase
import com.example.reminderapp.presentation.base.BaseViewModel
import com.example.reminderapp.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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
    private val getTasksWithFlagCountUseCase: GetTasksWithFlagCountUseCase,
    private val getAllTasksCountUseCase: GetAllTasksCountUseCase,
) : BaseViewModel<Unit, MainUiState>() {

    private val _uiState = MutableStateFlow<UiState<MainUiState>>(UiState.Loading)
    override val uiState: StateFlow<UiState<MainUiState>> = _uiState

    override fun fetchData(params: Unit) {
        viewModelScope.launch {
//            _uiState.value = UiState.Success(
//                MainUiState(
//                     TestData().getTestList(), 6, 5, 1, 2
//                )
//            )
//            TODO Делать так:

            combine(
                getAllGroupsUseCase(Unit),
                getTasksForTodayCountUseCase(Unit),
                getTasksPlannedCountUseCase(Unit),
                getTasksWithFlagCountUseCase(Unit),
                getAllTasksCountUseCase(Unit),
            ) { allGroups, todayCount, plannedCount, withFlagCount, allCount  ->
                MainUiState(
                    allGroups, todayCount, plannedCount, withFlagCount, allCount
                )
            }.catch { e ->
                _uiState.value = UiState.Error(e.toString())
            }.collect {
                _uiState.value = UiState.Success(it)
            }


        }
    }



}

package com.example.reminderapp.presentation.mainscreen

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.GetAllGroupsUseCase
import com.example.domain.use_case.GetAllTasksCountUseCase
import com.example.domain.use_case.GetNoTimeTasksCountUseCase
import com.example.domain.use_case.GetTasksForTodayCountUseCase
import com.example.domain.use_case.GetTasksPlannedCountUseCase
import com.example.domain.use_case.GetTasksWithFlagCountUseCase
import com.example.reminderapp.presentation.base.BaseViewModel
import com.example.reminderapp.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

data class MainUiState(
    val groups: List<Group>,
    val todayCount: Int,
    val plannedCount: Int,
    val withFlagCount: Int,
    val noTimeCount: Int,
    val allCount: Int
)

class MainViewModel(
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val getTasksForTodayCountUseCase: GetTasksForTodayCountUseCase,
    private val getTasksPlannedCountUseCase: GetTasksPlannedCountUseCase,
    private val getTasksWithFlagCountUseCase: GetTasksWithFlagCountUseCase,
    private val getAllTasksCountUseCase: GetAllTasksCountUseCase,
    private val getNoTimeTasksCountUseCase: GetNoTimeTasksCountUseCase,
) : BaseViewModel<Unit, MainUiState>() {

    private val _uiState = MutableStateFlow<UiState<MainUiState>>(UiState.Loading)
    override val uiState: StateFlow<UiState<MainUiState>> = _uiState

    override fun fetchData(params: Unit) {
        viewModelScope.launch {
            val groupsFlow = getAllGroupsUseCase(Unit)
            val todayCountFlow = getTasksForTodayCountUseCase(Unit)
            val plannedCountFlow = getTasksPlannedCountUseCase(Unit)
            val withFlagCountFlow = getTasksWithFlagCountUseCase(Unit)
            val noTimeCountFlow = getNoTimeTasksCountUseCase(Unit)
            val allCountFlow = getAllTasksCountUseCase(Unit)

            combine(
                groupsFlow,
                todayCountFlow,
                plannedCountFlow,
                withFlagCountFlow
            ) { allGroups, todayCount, plannedCount, withFlagCount ->
                CombinedResult(
                    groups = allGroups,
                    todayCount = todayCount,
                    plannedCount = plannedCount,
                    withFlagCount = withFlagCount
                )
            }.flatMapLatest { combinedResult ->
                combine(
                    noTimeCountFlow,
                    allCountFlow
                ) { noTimeCount, allCount ->
                    MainUiState(
                        groups = combinedResult.groups,
                        todayCount = combinedResult.todayCount,
                        plannedCount = combinedResult.plannedCount,
                        withFlagCount = combinedResult.withFlagCount,
                        noTimeCount = noTimeCount,
                        allCount = allCount
                    )
                }
            }.catch { e ->
                _uiState.value = UiState.Error(e.toString())
            }.collect { uiState ->
                _uiState.value = UiState.Success(uiState)
            }
        }
    }

    private data class CombinedResult(
        val groups: List<Group>,
        val todayCount: Int,
        val plannedCount: Int,
        val withFlagCount: Int
    )
}
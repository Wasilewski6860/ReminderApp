package com.example.reminderapp.presentation.mainscreen

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.group.GetAllGroupsUseCase
import com.example.domain.use_case.task.GetAllTasksCountUseCase
import com.example.domain.use_case.task.GetNoTimeTasksCountUseCase
import com.example.domain.use_case.task.GetTasksForTodayCountUseCase
import com.example.domain.use_case.task.GetTasksPlannedCountUseCase
import com.example.domain.use_case.task.GetTasksWithFlagCountUseCase
import com.example.domain.use_case.task.GetTasksWithoutGroupCountUseCase
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
    val allCount: Int,
    val withoutGroupCount: Int
)

class MainViewModel(
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val getTasksForTodayCountUseCase: GetTasksForTodayCountUseCase,
    private val getTasksPlannedCountUseCase: GetTasksPlannedCountUseCase,
    private val getTasksWithFlagCountUseCase: GetTasksWithFlagCountUseCase,
    private val getAllTasksCountUseCase: GetAllTasksCountUseCase,
    private val getNoTimeTasksCountUseCase: GetNoTimeTasksCountUseCase,
    private val getTasksWithoutGroupCountUseCase: GetTasksWithoutGroupCountUseCase
) : BaseViewModel<Unit, MainUiState>() {

    private val _uiState = MutableStateFlow<UiState<MainUiState>>(UiState.Loading)
    override val uiState: StateFlow<UiState<MainUiState>> = _uiState

    override fun fetchData(params: Unit) {
        viewModelScope.launch {
            val groupsFlow = getAllGroupsUseCase()
            val todayCountFlow = getTasksForTodayCountUseCase()
            val plannedCountFlow = getTasksPlannedCountUseCase()
            val withFlagCountFlow = getTasksWithFlagCountUseCase()
            val noTimeCountFlow = getNoTimeTasksCountUseCase()
            val allCountFlow = getAllTasksCountUseCase()
            val withoutGroupCountFlow = getTasksWithoutGroupCountUseCase()

            combine(
                groupsFlow,
                todayCountFlow,
                plannedCountFlow,
                withFlagCountFlow,
                withoutGroupCountFlow
            ) { allGroups, todayCount, plannedCount, withFlagCount, withoutGroupCount ->
                CombinedResult(
                    groups = allGroups,
                    todayCount = todayCount,
                    plannedCount = plannedCount,
                    withFlagCount = withFlagCount,
                    withoutGroupCount = withoutGroupCount
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
                        allCount = allCount,
                        withoutGroupCount = combinedResult.withoutGroupCount
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
        val withFlagCount: Int,
        val withoutGroupCount: Int
    )
}
package com.example.reminderapp.presentation.reminder_list

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.use_case.EditTaskUseCase
import com.example.domain.use_case.GetAllTasksUseCase
import com.example.domain.use_case.GetGroupWithTasksUseCase
import com.example.domain.use_case.GetPlannedTasksUseCase
import com.example.domain.use_case.GetTasksForTodayUseCase
import com.example.domain.use_case.GetTasksWithFlagUseCase
import com.example.reminderapp.R
import com.example.reminderapp.app.App
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.navigation.TasksListTypeCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class RemindersListUiState(
    val tasks: List<Task>,
    val groupName: String
)

class ReminderListViewModel(
    application: Application,
    private val getGroupWithTasksUseCase: GetGroupWithTasksUseCase,
    private val getTasksForTodayUseCase: GetTasksForTodayUseCase,
    private val getPlannedTasksUseCase: GetPlannedTasksUseCase,
    private val getTasksWithFlagUseCase: GetTasksWithFlagUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val editTaskUseCase: EditTaskUseCase
) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel

    private val _uiState = MutableStateFlow<UiState<RemindersListUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<RemindersListUiState>> = _uiState

    fun fetchData(taskListType: TasksListTypeCase) {
        val context: Context = getApplication<App>().applicationContext
        viewModelScope.launch {
            when(taskListType) {
                is TasksListTypeCase.GroupTasks -> {
                    getGroupWithTasksUseCase(taskListType.groupId)
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(RemindersListUiState(it.tasks, it.group.groupName))
                        }
                }
                TasksListTypeCase.PlannedTasks -> {
                    getPlannedTasksUseCase(Unit)
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.planned))
                            )
                        }
                }
                TasksListTypeCase.TasksWithFlag -> {
                    getTasksWithFlagUseCase(Unit)
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.with_flag))
                            )
                        }
                }
                TasksListTypeCase.TodayTasks -> {
                    getTasksForTodayUseCase(Unit)
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.current_day))
                            )
                        }
                }

                TasksListTypeCase.AllTasks -> {
                    getAllTasksUseCase(Unit)
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.all))
                            )
                        }
                }
            }
        }
    }

    fun editTask(
        taskId: Int,
        taskName: String,
        taskDesc: String,
        taskCreationTime: Long,
        taskTime: Long,
        taskTimePeriod: Long,
        taskType: TaskPeriodType,
        isActive: Boolean,
        isMarkedWithFlag: Boolean,
        groupId: Int,
        taskColor: Int
    ) {
        viewModelScope.launch {
            val task = Task(
                id = taskId,
                name = taskName,
                description = taskDesc,
                reminderCreationTime = taskCreationTime,
                reminderTime = taskTime,
                reminderTimePeriod = taskTimePeriod,
                type = taskType,
                isActive = isActive,
                isMarkedWithFlag = isMarkedWithFlag,
                groupId = groupId,
                color = taskColor
            )
            editTaskUseCase(
                task
            )
        }
    }

    fun deleteTask(task: Task) {
        /** Deletion process here **/
    }

}
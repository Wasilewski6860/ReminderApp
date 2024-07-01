package com.example.reminderapp.presentation.reminder_list

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.use_case.task.DeleteTaskUseCase
import com.example.domain.use_case.task.EditTaskUseCase
import com.example.domain.use_case.task.GetAllTasksUseCase
import com.example.domain.use_case.group.GetGroupWithTasksUseCase
import com.example.domain.use_case.reminder.DeleteReminderUseCase
import com.example.domain.use_case.reminder.EditReminderUseCase
import com.example.domain.use_case.task.GetNoTimeTasksUseCase
import com.example.domain.use_case.task.GetPlannedTasksUseCase
import com.example.domain.use_case.task.GetTasksForTodayUseCase
import com.example.domain.use_case.task.GetTasksWithFlagUseCase
import com.example.domain.use_case.task.GetTasksWithoutGroupUseCase
import com.example.reminderapp.R
import com.example.reminderapp.app.App
import com.example.reminderapp.presentation.base.OperationResult
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.navigation.TasksListTypeCase
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
    private val getNoTimeTasksUseCase: GetNoTimeTasksUseCase,
    private val editReminderUseCase: EditReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val getTasksWithoutGroupUseCase: GetTasksWithoutGroupUseCase
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<UiState<RemindersListUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<RemindersListUiState>> = _uiState

    private val _operationResult =
        MutableStateFlow<OperationResult<Unit>>(OperationResult.NotStarted)
    val operationResult: StateFlow<OperationResult<Unit>> = _operationResult

    private var _floatingActionButtonVisibility = MutableStateFlow(false)
    val floatingActionButtonVisibility get() = _floatingActionButtonVisibility

    fun fetchData(taskListType: TasksListTypeCase) {
        val context: Context = getApplication<App>().applicationContext
        viewModelScope.launch {
            when (taskListType) {
                is TasksListTypeCase.GroupTasks -> {
                    getGroupWithTasksUseCase(taskListType.groupId)
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value =
                                UiState.Success(RemindersListUiState(it.tasks, it.group.groupName))
                            _floatingActionButtonVisibility.value = true
                        }
                }

                TasksListTypeCase.PlannedTasks -> {
                    getPlannedTasksUseCase()
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.planned))
                            )
                            _floatingActionButtonVisibility.value = false
                        }
                }

                TasksListTypeCase.TasksWithFlag -> {
                    getTasksWithFlagUseCase()
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.with_flag))
                            )
                            _floatingActionButtonVisibility.value = false
                        }
                }

                TasksListTypeCase.TodayTasks -> {
                    getTasksForTodayUseCase()
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.current_day))
                            )
                            _floatingActionButtonVisibility.value = false
                        }
                }

                TasksListTypeCase.AllTasks -> {
                    getAllTasksUseCase()
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.all))
                            )
                            _floatingActionButtonVisibility.value = false
                        }
                }

                TasksListTypeCase.TasksNoTime -> {
                    getNoTimeTasksUseCase()
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.no_time))
                            )
                            _floatingActionButtonVisibility.value = false
                        }
                }

                TasksListTypeCase.TaskWithoutGroup -> {
                    getTasksWithoutGroupUseCase()
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                        .collect {
                            _uiState.value = UiState.Success(
                                RemindersListUiState(it, context.getString(R.string.without_group))
                            )
                            _floatingActionButtonVisibility.value = false
                        }
                }
            }
        }
    }

    fun editIsActive(
        task: Task,
        isActive: Boolean
    ) {
        viewModelScope.launch {
            task.isActive = isActive
            editReminderUseCase(
                task
            )
        }
    }

    fun deleteTask(
        task: Task
    ) {
        _operationResult.value = OperationResult.Loading
        viewModelScope.launch {
            try {
                deleteReminderUseCase(task)
                _operationResult.value = OperationResult.Success(Unit)
            } catch (e: Exception) {
                _operationResult.value = OperationResult.Error(e.toString())
            }
        }
    }

}
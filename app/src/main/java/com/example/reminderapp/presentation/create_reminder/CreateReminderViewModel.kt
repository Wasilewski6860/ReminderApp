package com.example.reminderapp.presentation.create_reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.use_case.EditTaskUseCase
import com.example.domain.use_case.GetAllGroupsUseCase
import com.example.domain.use_case.SaveTaskUseCase
import com.example.reminderapp.presentation.base.OperationResult
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.utils.TimePeriodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CreateReminderViewModel(
    val createTaskUseCase: SaveTaskUseCase,
    val getAllGroupsUseCase: GetAllGroupsUseCase,
    val editTaskUseCase: EditTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Group>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Group>>> = _uiState

    private val _saveResult = MutableStateFlow<OperationResult<Task>>(OperationResult.NotStarted)
    val saveResult: StateFlow<OperationResult<Task>> = _saveResult

    fun fetchGroups() {
        viewModelScope.launch {
//            _uiState.value = UiState.Success(TestData().getTestList())
//            TODO использовать это
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
       taskId: Int? = null,
       taskName: String,
       taskDesc: String,
       taskCreationTime: Long,
       taskType: TaskPeriodType,
       taskTime: Long?,
       taskTimePeriod: Long?,
       isActive: Boolean,
       isMarkedWithFlag: Boolean,
       groupId: Int,
       taskColor: Int
    ) {
        viewModelScope.launch {
            if (taskId == null) {
                createTaskUseCase(
                    Task(
                        id = 0,
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
                )
            } else {
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
                _saveResult.value = OperationResult.Success(task)
            }
        }
    }

}
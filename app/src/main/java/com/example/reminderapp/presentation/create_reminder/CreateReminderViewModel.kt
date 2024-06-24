package com.example.reminderapp.presentation.create_reminder

import android.icu.util.Calendar
import android.text.Editable
import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


data class CreateReminderScreenState(
    val taskId: Int? = null,
    val reminderName: String = "",
    val reminderDescription: String = "",
//    val switchSelected: Boolean = false,
    val reminderFirstTime: Long? = null,
    val reminderPeriod: Long? = null,
    val reminderFlag: Boolean= false,
    val reminderColor: Int? = null,
    val groupsUiState: UiState<List<Group>> = UiState.Loading,
    val groupLoaded: Boolean = false,
    val reminderGroupId: Int? = null,
    val reminderGroupName: String? = null
)

sealed interface ValidationResult {
    object NotStarted: ValidationResult
    object IncorrectName: ValidationResult
    object IncorrectGroup: ValidationResult
    object IncorrectColor: ValidationResult
}

class CreateReminderViewModel(
    val createTaskUseCase: SaveTaskUseCase,
    val getAllGroupsUseCase: GetAllGroupsUseCase,
    val editTaskUseCase: EditTaskUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(CreateReminderScreenState())
    val screenState: StateFlow<CreateReminderScreenState> = _screenState

    private var currentState: CreateReminderScreenState = screenState.value

    private val _validationResult = MutableStateFlow<ValidationResult>(ValidationResult.NotStarted)
    val validationResult: StateFlow<ValidationResult> = _validationResult

    private val _saveResult = MutableStateFlow<OperationResult<Unit>>(OperationResult.NotStarted)
    val saveResult: StateFlow<OperationResult<Unit>> = _saveResult

    fun onTaskIdChanged(input: Int?) {
        currentState = screenState.value
        if (currentState.taskId != input) {
            _screenState.value = _screenState.value.copy(
                taskId = input
            )
        }
    }

    fun onNameTextChanged(input: Editable?) {
        onNameTextChanged(input.toString())
    }

    fun onNameTextChanged(input: String) {
        currentState = screenState.value
        if (currentState.reminderName != input) {
            _screenState.value = _screenState.value.copy(
                reminderName = input
            )
        }
    }

    fun onDescriptionTextChanged(input: Editable?) {
        onDescriptionTextChanged(input.toString())
    }

    fun onDescriptionTextChanged(input: String) {
        currentState = screenState.value
        if (currentState.reminderDescription != input) {
            _screenState.value = _screenState.value.copy(
                reminderDescription = input
            )
        }
    }

    fun onRemindSwitchChecked(isChecked: Boolean) {
//        _screenState.value = _screenState.value.copy(
//            switchSelected = isChecked
//        )
        if(!isChecked){
            currentState = screenState.value
            if (currentState.reminderFirstTime != null || currentState.reminderPeriod != null) {
                _screenState.value = _screenState.value.copy(
                    reminderFirstTime = null,
                    reminderPeriod = null
                )
            }
        }
    }

    fun onFirstTimeChanged(input: Long?) {
        currentState = screenState.value
        if (currentState.reminderFirstTime != input) {
            _screenState.value = _screenState.value.copy(
                reminderFirstTime = input
            )
        }
    }

    fun onPeriodChanged(input: Long?) {
        currentState = screenState.value
        if (currentState.reminderPeriod != input) {
            _screenState.value = _screenState.value.copy(
                reminderPeriod = input
            )
        }
    }

    fun onFlagChanged(input: Boolean) {
        currentState = screenState.value
        if (currentState.reminderFlag != input) {
            _screenState.value = _screenState.value.copy(
                reminderFlag = input
            )
        }
    }

    fun onColorChanged(input: Int?) {
        currentState = screenState.value
        if (currentState.reminderColor != input) {
            _screenState.value = _screenState.value.copy(
                reminderColor = input
            )
        }
    }

    fun onGroupIdChanged(input: Int?) {
        currentState = screenState.value
        if (currentState.reminderGroupId != input) {
            _screenState.value = _screenState.value.copy(
                reminderGroupId = input
            )
        }
    }

    fun onGroupNameChanged(input: String?) {
        currentState = screenState.value
        if (currentState.reminderGroupName != input) {
            _screenState.value = _screenState.value.copy(
                reminderGroupName = input
            )
        }
    }

    fun onGroupLoadedToSpinner() {
        _screenState.value = _screenState.value.copy(
            groupLoaded = true
        )
    }

    fun fetchGroups() {
        viewModelScope.launch {
            getAllGroupsUseCase(Unit)
                .onStart {
                    _screenState.value = _screenState.value.copy(groupsUiState = UiState.Loading)
                }
                .catch { exception ->
                    _screenState.value = _screenState.value.copy(groupsUiState = UiState.Error(exception.toString()))
                }
                .collect { groups ->
                    _screenState.value = _screenState.value.copy(groupsUiState = UiState.Success(groups), groupLoaded = false)
                }
        }
    }

    fun validate(state: CreateReminderScreenState): Boolean {
        if(state.reminderName.isEmpty()){
            _validationResult.value = ValidationResult.IncorrectName
            return false
        }
        if(state.reminderGroupId==null){
            _validationResult.value = ValidationResult.IncorrectGroup
            return false
        }
        if(state.reminderColor==null){
            _validationResult.value = ValidationResult.IncorrectColor
            return false
        }
        return true
    }
    fun saveTask() {
        viewModelScope.launch {
            val currentState: CreateReminderScreenState = _screenState.value
            if(validate(currentState)) {
                with(currentState) {
                    var taskType = TaskPeriodType.NO_TIME
                    var selectedTime = currentState.reminderFirstTime
                    if(currentState.reminderFirstTime==null && currentState.reminderPeriod==null) {
                        taskType = TaskPeriodType.NO_TIME
                    }
                    if(currentState.reminderFirstTime==null && currentState.reminderPeriod!= null) {
                        selectedTime=Calendar.getInstance().timeInMillis
                        taskType=TaskPeriodType.PERIODIC
                    }
                    if (currentState.reminderFirstTime!=null && currentState.reminderPeriod==null){
                        taskType=TaskPeriodType.ONE_TIME
                    }
                    if (taskId == null) {
                        val task = Task(
                            id = 0,
                            name = currentState.reminderName,
                            description = currentState.reminderDescription,
                            reminderCreationTime = Calendar.getInstance().timeInMillis,
                            reminderTime = selectedTime,
                            reminderTimePeriod = currentState.reminderPeriod,
                            type = taskType,
                            isActive = true,
                            isMarkedWithFlag = currentState.reminderFlag,
                            groupId = currentState.reminderGroupId!!,
                            color = currentState.reminderColor!!
                        )
                        createTaskUseCase(
                            task
                        )
                        .catch {
                            _saveResult.value = OperationResult.Error(it.toString())
                        }
                        .collect{
                            _saveResult.value = OperationResult.Success(Unit)
                        }
                        Log.d("SaveTask", "Task created successfully")

                    } else {
                        val task = Task(
                            id = taskId,
                            name = currentState.reminderName,
                            description = currentState.reminderDescription,
                            reminderCreationTime = Calendar.getInstance().timeInMillis,
                            reminderTime = selectedTime,
                            reminderTimePeriod = currentState.reminderPeriod,
                            type = taskType,
                            isActive = true,
                            isMarkedWithFlag = currentState.reminderFlag,
                            groupId = currentState.reminderGroupId!!,
                            color = currentState.reminderColor!!
                        )
                        editTaskUseCase(
                            task
                        )
                        Log.d("SaveTask", "Task created successfully")
                        _saveResult.value = OperationResult.Success(Unit)
                    }
                }
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

            }
        }
    }

}
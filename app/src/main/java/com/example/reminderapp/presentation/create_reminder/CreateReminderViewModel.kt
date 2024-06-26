package com.example.reminderapp.presentation.create_reminder

import android.icu.util.Calendar
import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.use_case.group.GetAllGroupsUseCase
import com.example.domain.use_case.reminder.CreateReminderUseCase
import com.example.domain.use_case.reminder.EditReminderUseCase
import com.example.reminderapp.presentation.base.ICalendarProvider
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
    val groupsUiState: UiState<List<Group>> = UiState.Loading,
    val groupLoaded: Boolean = false,
    val periodsLoaded: Boolean = false,
    val reminderGroupId: Int? = null,
    val reminderGroupName: String? = null
)

sealed interface ValidationResult {
    object NotStarted: ValidationResult
    object IncorrectName: ValidationResult
    object IncorrectGroup: ValidationResult
}

class CreateReminderViewModel(
    private val createReminderUseCase: CreateReminderUseCase,
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val editReminderUseCase: EditReminderUseCase,
    private val calendarProvider: ICalendarProvider
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
        if(input==null && !currentState.periodsLoaded){
            _screenState.value = _screenState.value.copy(
                periodsLoaded = true
            )
        }
        else
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
            getAllGroupsUseCase()
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

    private fun validate(state: CreateReminderScreenState): Boolean {
        if(state.reminderName.isEmpty()) {
            _validationResult.value = ValidationResult.IncorrectName
            return false
        }
//        if(state.reminderGroupId==null) {
//            _validationResult.value = ValidationResult.IncorrectGroup
//            return false
//        }
        return true
    }

    fun validate(): Boolean = validate(screenState.value)

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
                        selectedTime=calendarProvider.getInstance().timeInMillis
                        taskType=TaskPeriodType.PERIODIC
                    }
                    if(currentState.reminderPeriod!= null ) taskType=TaskPeriodType.PERIODIC
                    if (currentState.reminderFirstTime!=null && currentState.reminderPeriod==null){
                        taskType=TaskPeriodType.ONE_TIME
                    }
                    if (taskId == null) {
                        val task = Task(
                            id = 0,
                            name = currentState.reminderName,
                            description = currentState.reminderDescription,
                            reminderCreationTime = calendarProvider.getInstance().timeInMillis,
                            reminderTime = selectedTime,
                            reminderTimePeriod = currentState.reminderPeriod,
                            type = taskType,
                            isActive = true,
                            isMarkedWithFlag = currentState.reminderFlag,
                            groupId = currentState.reminderGroupId
                        )
                        createReminderUseCase(task)
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
                            reminderCreationTime = calendarProvider.getInstance().timeInMillis,
                            reminderTime = selectedTime,
                            reminderTimePeriod = currentState.reminderPeriod,
                            type = taskType,
                            isActive = true,
                            isMarkedWithFlag = currentState.reminderFlag,
                            groupId = currentState.reminderGroupId
                        )
                        editReminderUseCase(task)
                        Log.d("SaveTask", "Task edited successfully")
                        _saveResult.value = OperationResult.Success(Unit)
                    }
                }
            }
        }
    }

}
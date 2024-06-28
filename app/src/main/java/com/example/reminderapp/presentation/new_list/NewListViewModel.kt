package com.example.reminderapp.presentation.new_list

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.group.EditGroupUseCase
import com.example.domain.use_case.group.InsertGroupUseCase
import com.example.reminderapp.presentation.base.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NewListScreenState(
    val groupId: Int? = null,
    val groupName: String = "",
    val groupColor: Int = 0,
    val groupImage: Int? = null,
    val isImageVisible: Boolean = true,
    val tasksCount: Int = 0,
)

sealed interface ValidationResult {
    object NotStarted: ValidationResult
    object IncorrectName: ValidationResult
    object IncorrectColor: ValidationResult
}

class NewListViewModel(
    private val insertGroupUseCase: InsertGroupUseCase,
    private val editGroupUseCase: EditGroupUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(NewListScreenState())
    val screenState: StateFlow<NewListScreenState> = _screenState

    private val _validationResult = MutableStateFlow<ValidationResult>(ValidationResult.NotStarted)
    val validationResult: StateFlow<ValidationResult> = _validationResult

    private var currentState = screenState.value

    private val _saveResult = MutableStateFlow<OperationResult<Long>>(OperationResult.NotStarted)
    val saveResult: StateFlow<OperationResult<Long>> = _saveResult

    fun onGroupIdChanged(input: Int?) {
        currentState = screenState.value
        if (currentState.groupId != input) {
            _screenState.value = _screenState.value.copy(
                groupId = input
            )
        }
    }

    fun onGroupNameChanged(input: String) {
        currentState = screenState.value
        if (currentState.groupName != input) {
            _screenState.value = _screenState.value.copy(
                groupName = input
            )
        }
    }

    fun onGroupNameChanged(input: Editable?) {
        onGroupNameChanged(input.toString())
    }

    fun onGroupColorChanged(input: Int) {
        currentState = screenState.value
        if (currentState.groupColor != input) {
            _screenState.value = _screenState.value.copy(
                groupColor = input
            )
        }
    }

    fun onGroupImageChanged(input: Int?) {
        currentState = screenState.value
        if (currentState.groupColor != input) {
            _screenState.value = _screenState.value.copy(
                groupImage = input
            )
        }
    }

    fun onGroupImageVisibilityChanged(input: Boolean) {
        currentState = screenState.value
        if (currentState.isImageVisible != input) {
            _screenState.value = _screenState.value.copy(
                isImageVisible = input
            )
        }
    }

    fun isImageInState(selectedItem: Int): Boolean {
        currentState = screenState.value
        return currentState.groupImage!=null && currentState.groupImage == selectedItem
    }

    fun onGroupTasksCountChanged(input: Int) {
        currentState = screenState.value
        if (currentState.tasksCount != input) {
            _screenState.value = _screenState.value.copy(
                tasksCount = input
            )
        }
    }

    fun validate(state: NewListScreenState): Boolean {
        if(state.groupName.isEmpty()){
            _validationResult.value = ValidationResult.IncorrectName
            return false
        }
        if(state.groupColor==null){
            _validationResult.value = ValidationResult.IncorrectColor
            return false
        }
        return true
    }

    fun saveList() {
        viewModelScope.launch {
            currentState = screenState.value
            if(validate(currentState)) {
                val group = Group(
                    groupId = currentState.groupId?:0,
                    groupName = currentState.groupName,
                    groupColor = currentState.groupColor,
                    groupImage = currentState.groupImage,
                    tasksCount = currentState.tasksCount
                )
                try {
                    if (currentState.groupId==null){
                        val fetchId = insertGroupUseCase(group)
                        _saveResult.value = OperationResult.Success(fetchId)
                    }
                    else {
                        editGroupUseCase(group)
                        _saveResult.value = OperationResult.Success(currentState.groupId!!.toLong())
                    }
                }
                catch (e: Exception) {
                    _saveResult.value = OperationResult.Error(e.toString())
                }
            }
        }
    }

}
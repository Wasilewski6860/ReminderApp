package com.example.reminderapp.presentation.new_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.use_case.EditGroupUseCase
import com.example.domain.use_case.InsertGroupUseCase
import com.example.reminderapp.presentation.base.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewListViewModel(
    val insertGroupUseCase: InsertGroupUseCase,
    val editGroupUseCase: EditGroupUseCase,
) : ViewModel() {

    private val _saveResult = MutableStateFlow<OperationResult<Long>>(OperationResult.NotStarted)
    val saveResult: StateFlow<OperationResult<Long>> = _saveResult

    fun saveList(
        id: Int? = null,
        groupName: String,
        groupColor: Int,
        groupImage: Int?,
        tasksCount: Int,
    ) {
        viewModelScope.launch {
            val group = Group(
                groupId = id?:0,
                groupName = groupName,
                groupColor = groupColor,
                groupImage = groupImage,
                tasksCount = tasksCount
            )
            try {
                if (id==null){
                    val fetchId = insertGroupUseCase(group)
                    _saveResult.value = OperationResult.Success(fetchId)
                }
                else {
                    editGroupUseCase(group)
                    _saveResult.value = OperationResult.Success(id.toLong())
                }
            }
            catch (e: Exception) {
                _saveResult.value = OperationResult.Error(e.toString())
            }

        }
    }

}
package com.example.reminderapp.presentation.new_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.InsertGroupUseCase
import com.example.reminderapp.presentation.base.BaseViewModel
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.mainscreen.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class NewListViewModel(
    val insertGroupUseCase: InsertGroupUseCase
) : ViewModel() {
    fun saveList(
        id: Int = -1,
        groupName: String,
        groupColor: Int,
        groupImage: Int,
        tasksCount: Int,
    ) {
        viewModelScope.launch {
            insertGroupUseCase(
                Group(id, groupName, groupColor, groupImage, tasksCount)
            )
        }
    }

}
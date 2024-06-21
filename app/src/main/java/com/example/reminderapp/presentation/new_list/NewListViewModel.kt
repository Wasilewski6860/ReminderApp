package com.example.reminderapp.presentation.new_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.InsertGroupUseCase
import kotlinx.coroutines.launch

class NewListViewModel(
    val insertGroupUseCase: InsertGroupUseCase
) : ViewModel() {
    fun saveList(
        id: Int? = null,
        groupName: String,
        groupColor: Int,
        groupImage: Int?,
        tasksCount: Int,
    ) {
        viewModelScope.launch {
            insertGroupUseCase(
                Group(
                    groupId = id ?: 0,
                    groupName = groupName,
                    groupColor = groupColor,
                    groupImage = groupImage,
                    tasksCount = tasksCount
                )
            )
        }
    }

}
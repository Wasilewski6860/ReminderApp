package com.example.reminderapp.presentation.editorlistsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.GetAllGroupsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ListsEditorViewModel(
    private val getAllGroupsUseCase: GetAllGroupsUseCase
    /** delete group use case params here */
) : ViewModel() {

    private val groupsListFlowData = MutableStateFlow<List<Group>>(emptyList())
    val groupsListData get() = groupsListFlowData

    fun fetchGroupsFromDatabase() {
        viewModelScope.launch {
            getAllGroupsUseCase.execute()
                .catch { e ->
                    Log.e("FETCHING DATA FROM DATABASE", e.toString())
                }
                .collect {
                    groupsListFlowData.value = it
                }
        }
    }

    fun deleteGroup(group: Group) {
        viewModelScope.launch {
            /** use case execute method here */
        }
    }

}
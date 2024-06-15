package com.example.reminderapp.presentation.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.GetAllGroupsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAllGroupsUseCase: GetAllGroupsUseCase
) : ViewModel() {

    private val groupsListFlowData = MutableStateFlow<List<Group>>(emptyList())
    val groupsListData get() = groupsListFlowData

    fun fetchTaskGroups() {
        viewModelScope.launch {
            getAllGroupsUseCase(Unit)
                .catch { e ->
                    Log.e("FETCHING DATA FROM DATABASE", e.toString())
                }
                .collect {
                    groupsListFlowData.value = it
                }
        }
    }
    
}
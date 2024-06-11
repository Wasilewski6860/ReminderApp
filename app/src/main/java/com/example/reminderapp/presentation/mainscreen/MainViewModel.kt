package com.example.reminderapp.presentation.mainscreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.GetAllGroupsUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAllGroupsUseCase: GetAllGroupsUseCase
) : ViewModel() {

    private val groupsListLiveData = MutableLiveData<List<Group>>()
    val groupsListData get() = groupsListLiveData

    fun fetchTaskGroups() {
        viewModelScope.launch {
            try {
                groupsListLiveData.postValue(getAllGroupsUseCase.execute())
                /** tasksListLiveData.postValue(...execute()) */
            } catch (e: Exception) {
                Log.e("FETCHING INFO FROM DATABASE", e.toString())
            }
        }
    }
    
}
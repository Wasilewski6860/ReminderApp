package com.example.reminderapp.presentation.mainscreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.GetAllTasksUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
    /** Get all Task lists UseCase param here */
) : ViewModel() {

    private val tasksListLiveData = MutableLiveData<List<Task>>()
    val tasksListData get() = tasksListLiveData

    fun fetchTaskGroups() {
        viewModelScope.launch {
            try {
                // Use case execute method here
                /** tasksListLiveData.postValue(...execute()) */
            } catch (e: Exception) {
                Log.e("FETCHING INFO FROM DATABASE", e.toString())
            }
        }
    }
    
}
package com.example.reminderapp.presentation.mainscreen

import androidx.lifecycle.LiveData
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
) : ViewModel() {

    private val tasksListLiveData = MutableLiveData<MutableList<Task>>()

    fun getTasksListLiveData(): LiveData<MutableList<Task>> {
        return tasksListLiveData
    }

    fun getAllTasks() {

    }

}
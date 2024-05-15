package com.example.reminderapp.viewmodels.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.Task
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.GetAllTasksUseCase

class MainViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val tasksListLiveData = MutableLiveData<MutableList<Task>>()

    fun getTasksListLiveData(): MutableLiveData<MutableList<Task>> {
        return tasksListLiveData
    }

    fun getAllTasks() {
        
    }

}
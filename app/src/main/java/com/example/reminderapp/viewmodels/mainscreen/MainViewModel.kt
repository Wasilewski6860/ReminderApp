package com.example.reminderapp.viewmodels.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.Task

class MainViewModel : ViewModel() {

    private val tasksListLiveData = MutableLiveData<MutableList<Task>>()

    fun getTasksListLiveData(): MutableLiveData<MutableList<Task>> {
        return tasksListLiveData
    }

    suspend fun getAllTasks() {
        
    }

}
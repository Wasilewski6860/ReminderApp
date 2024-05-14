package com.example.reminderapp.viewmodels.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.repositories.TaskRepositoryImpl
import com.example.domain.model.Task
import com.example.domain.use_case.GetAllTasksUseCase

class MainViewModel : ViewModel() {

    private val tasksListLiveData = MutableLiveData<MutableList<Task>>()
    private val getAllTasksUseCase = GetAllTasksUseCase(taskRepository = TaskRepositoryImpl())

    fun getTasksListLiveData(): MutableLiveData<MutableList<Task>> {
        return tasksListLiveData
    }

    suspend fun getAllTasks() {
        tasksListLiveData.value = getAllTasksUseCase.execute()
    }

}
package com.example.reminderapp.presentation.mainscreen

import android.util.Log
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

    private val tasksListLiveData = MutableLiveData<List<Task>>()

    fun getTasksListLiveData(): LiveData<List<Task>> {
        return tasksListLiveData
    }

    fun fetchTasksFromDatabase() {
        viewModelScope.launch {
            try {
                tasksListLiveData.postValue(getAllTasksUseCase.execute())
            } catch (e: Exception) {
                Log.e("loading tasks from room process", e.toString())
            }
        }
    }

    fun deleteTask(task: Task) {

    }

}
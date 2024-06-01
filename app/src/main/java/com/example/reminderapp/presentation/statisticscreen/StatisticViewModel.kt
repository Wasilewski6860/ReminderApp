package com.example.reminderapp.presentation.statisticscreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.use_case.GetAllTasksUseCase
import kotlinx.coroutines.launch

class StatisticViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {

    private val tasksListMutableLiveData = MutableLiveData<List<Task>>()
    val tasksListLiveData get() = tasksListMutableLiveData

    fun fetchTasksFromDatabase() {
        viewModelScope.launch {
            try {
                tasksListMutableLiveData.postValue(getAllTasksUseCase.execute())
            } catch (e: Exception) {
                Log.e("Getting data from database process", e.toString())
            }
        }
    }

}
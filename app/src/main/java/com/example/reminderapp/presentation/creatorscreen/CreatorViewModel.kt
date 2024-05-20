package com.example.reminderapp.presentation.creatorscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.use_case.GetTaskUseCase
import com.example.domain.use_case.SaveTaskUseCase
import kotlinx.coroutines.launch

class CreatorViewModel(
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase
) : ViewModel() {

    fun saveTaskInDatabase(task: Task) {
        viewModelScope.launch {
            try {
                saveTaskUseCase.execute(task)
            } catch (e: Exception) {
                Log.e("saving task in room process", e.toString())
            }
        }
    }

    fun getTaskFromDatabase() {
        viewModelScope.launch {
            try {
                // getTaskUseCase execute method here
            } catch (e: Exception) {
                Log.e("getting task from room process", e.toString())
            }
        }
    }

}
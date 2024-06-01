package com.example.reminderapp.presentation.creatorscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.EditTaskUseCase
import com.example.domain.use_case.GetTaskUseCase
import com.example.domain.use_case.SaveTaskUseCase
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CreatorViewModel(
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
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

    fun editTaskInDatabase(task: Task) {
        viewModelScope.launch {
            try {
                editTaskUseCase.execute(task)
            } catch (e: Exception) {
                Log.e("editing task process", e.toString())
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase.execute(task)
            } catch (e: Exception) {
                Log.e("deleting task process", e.toString())
            }
        }
    }

    fun getTimeDifferenceInMilliseconds(time1: String, time2: String): Long {
        var difference: Long = 0
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val dateTime1 = LocalDateTime.parse(time1, formatter)
            val dateTime2 = LocalDateTime.parse(time2, formatter)
            difference = ChronoUnit.MILLIS.between(dateTime1, dateTime2)
        }
        return difference
    }

}
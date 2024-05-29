package com.example.reminderapp.presentation.creatorscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.use_case.GetTaskUseCase
import com.example.domain.use_case.SaveTaskUseCase
import com.example.reminderapp.reminder.work.RemindWorkManager
import kotlinx.coroutines.launch
import kotlin.random.Random

class CreatorViewModel(
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase
) : ViewModel() {

    fun saveTask() {
        // Saving process here
    }

}
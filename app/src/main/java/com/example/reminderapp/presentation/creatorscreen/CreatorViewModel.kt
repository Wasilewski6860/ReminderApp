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
    private val getTaskUseCase: GetTaskUseCase,
) : ViewModel() {

    val result: MutableLiveData<Long> = MutableLiveData<Long>()
    fun saveTask(
        name: String,
        description: String
    ) {
        // Saving process here
        viewModelScope.launch {
            val res = saveTaskUseCase.execute(
                task = Task(
                    id = 0,
                    name = name,
                    description = description,
                    timestamp = System.currentTimeMillis(),
                    timeTarget = 2 * 60 * 1000,
                    type = TaskPeriodType.PERIODIC,
                    color = 0
                )
            )
            result.postValue(res)

        }
    }

}
package com.example.reminderapp.presentation.creatorscreen

import androidx.lifecycle.ViewModel
import com.example.domain.use_case.GetTaskUseCase
import com.example.domain.use_case.SaveTaskUseCase

class CreatorViewModel(
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase
) : ViewModel() {

    fun saveTask() {
        // Saving process here
    }

}
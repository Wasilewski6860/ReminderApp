package com.example.reminderapp.viewmodels.creatorscreen

import androidx.lifecycle.ViewModel
import com.example.data.repositories.TaskRepositoryImpl
import com.example.domain.model.Task
import com.example.domain.use_case.SaveTaskUseCase

class CreatorViewModel : ViewModel() {

    private val saveTaskUseCase = SaveTaskUseCase(taskRepository = TaskRepositoryImpl())

    fun saveTask() {
        // Saving process here

        // Temp Task object, nvm
        val tempTask = Task(
            reminderName = "Some reminder",
            reminderDescription = "Some desc",
            reminderCardBackgroundColor = 1
        )
        saveTaskUseCase.execute(tempTask)
    }

}
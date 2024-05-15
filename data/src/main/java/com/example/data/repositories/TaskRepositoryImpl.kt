package com.example.data.repositories

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository

class TaskRepositoryImpl : TaskRepository {

    override fun saveTask(task: Task) {
        // reminder saving in room db process here
    }

    override suspend fun getAllTasks(): MutableList<Task> {
        // getting all tasks from room process here
        return mutableListOf() // <-- temp code string nvm
    }

    override fun getTask(): Task {
        return Task(
            reminderName = "",
            reminderDescription = "",
            reminderCardBackgroundColor = 0
        ) // <-- temp code tho
    }

    override fun deleteTask(task: Task) {

    }

    override fun deleteAllCurrentTasks() {

    }

}
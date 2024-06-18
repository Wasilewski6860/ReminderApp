package com.example.reminderapp.presentation.navigation

import com.google.gson.Gson
import org.koin.core.component.KoinComponent

sealed class TasksListTypeCase: KoinComponent {

    object AllTasks : TasksListTypeCase()

    object TodayTasks : TasksListTypeCase()

    object PlannedTasks : TasksListTypeCase()

    class GroupTasks(val groupId: Int) : TasksListTypeCase()

    object TasksWithFlag : TasksListTypeCase()

}

object TasksListTypeCaseSerializer {
    private val gson = Gson()

    fun serialize(typeCase: TasksListTypeCase): String {
        return gson.toJson(typeCase)
    }

    fun deserialize(data: String): TasksListTypeCase {
        return gson.fromJson(data, TasksListTypeCase::class.java)
    }

}
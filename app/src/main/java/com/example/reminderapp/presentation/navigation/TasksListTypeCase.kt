package com.example.reminderapp.presentation.navigation

sealed class TasksListTypeCase {

    object TodayTasks : TasksListTypeCase()

    object PlannedTasks : TasksListTypeCase()

    object AllTasks : TasksListTypeCase()

    object TasksWithFlag : TasksListTypeCase()

}
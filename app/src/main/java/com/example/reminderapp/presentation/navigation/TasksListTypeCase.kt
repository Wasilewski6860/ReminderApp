package com.example.reminderapp.presentation.navigation

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class TasksListTypeCase: KoinComponent {

    object AllTasks : TasksListTypeCase()
    object TodayTasks : TasksListTypeCase()

    object PlannedTasks : TasksListTypeCase()

    class GroupTasks(val groupId: Int) : TasksListTypeCase()

    object TasksWithFlag : TasksListTypeCase()

}
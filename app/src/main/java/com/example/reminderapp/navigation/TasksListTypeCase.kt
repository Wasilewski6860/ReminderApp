package com.example.reminderapp.navigation

import java.io.Serializable

sealed class TasksListTypeCase : Serializable {

    object AllTasks : TasksListTypeCase()

    object TodayTasks : TasksListTypeCase()

    object PlannedTasks : TasksListTypeCase()

    class GroupTasks(val groupId: Int) : TasksListTypeCase()

    object TasksWithFlag : TasksListTypeCase()

    object TasksNoTime : TasksListTypeCase()

    object TaskWithoutGroup : TasksListTypeCase()

}


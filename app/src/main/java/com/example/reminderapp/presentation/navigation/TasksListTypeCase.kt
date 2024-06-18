package com.example.reminderapp.presentation.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

sealed class TasksListTypeCase : Serializable {

//    @Parcelize
    object AllTasks : TasksListTypeCase()

//    @Parcelize
    object TodayTasks : TasksListTypeCase()

//    @Parcelize
    object PlannedTasks : TasksListTypeCase()

//    @Parcelize
    class GroupTasks(val groupId: Int) : TasksListTypeCase()

//    @Parcelize
    object TasksWithFlag : TasksListTypeCase()

}


package com.example.reminderapp.presentation

import android.content.Context
import com.example.domain.model.Group
import com.example.reminderapp.R
import com.example.reminderapp.reminder.RemindAlarmManager
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TestData: KoinComponent {


    private val context: Context by inject()

    fun getTestList(): List<Group> {
        return listOf(
            Group(
                groupId = 0,
                groupName = "First list",
                groupColor = context.getColor(R.color.red),
                tasksCount = 10
            ),
            Group(
                groupId = 1,
                groupName = "Second list",
                groupColor = context.getColor(R.color.black),
                tasksCount = 5
            ),
            Group(
                groupId = 2,
                groupName = "Third list",
                groupColor = context.getColor(R.color.blue),
                tasksCount = 3
            ),
            Group(
                groupId = 3,
                groupName = "Fourth list",
                groupColor =context.getColor(R.color.purple_500),
                tasksCount = 1
            )
        )
    }
}
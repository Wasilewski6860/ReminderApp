package com.example.reminderapp.presentation.base.serializer

import com.example.reminderapp.presentation.base.BaseDataSerializer
import com.example.reminderapp.presentation.navigation.TasksListTypeCase
import com.google.gson.Gson

object TasksListTypeCaseSerializer: BaseDataSerializer<TasksListTypeCase> {
    private val gson = Gson()

    override fun serialize(obj: TasksListTypeCase): String {
        return gson.toJson(obj)
    }

    override fun deserialize(serialized: String): TasksListTypeCase {
        return gson.fromJson(serialized, TasksListTypeCase::class.java)
    }

}


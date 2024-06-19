package com.example.reminderapp.presentation.base.serializer

import com.example.domain.model.Task
import com.example.reminderapp.presentation.base.BaseDataSerializer
import com.google.gson.Gson

object TaskSerializer: BaseDataSerializer<Task> {
    private val gson = Gson()

    override fun serialize(obj: Task): String {
        return gson.toJson(obj)
    }

    override fun deserialize(serialized: String): Task {
        return gson.fromJson(serialized, Task::class.java)
    }

}
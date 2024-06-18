package com.example.reminderapp.presentation.base.serializer

import com.example.domain.model.Group
import com.example.reminderapp.presentation.base.BaseDataSerializer
import com.google.gson.Gson

object GroupSerializer: BaseDataSerializer<Group> {
    private val gson = Gson()

    override fun serialize(group: Group): String {
        return gson.toJson(group)
    }

    override fun deserialize(data: String): Group {
        return gson.fromJson(data, Group::class.java)
    }
}
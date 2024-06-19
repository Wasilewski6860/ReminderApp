package com.example.reminderapp.presentation.base.serializer

import com.example.domain.model.Group
import com.example.reminderapp.presentation.base.BaseDataSerializer
import com.google.gson.Gson

object GroupSerializer: BaseDataSerializer<Group> {
    private val gson = Gson()

    override fun serialize(obj: Group): String {
        return gson.toJson(obj)
    }

    override fun deserialize(serialized: String): Group {
        return gson.fromJson(serialized, Group::class.java)
    }
}
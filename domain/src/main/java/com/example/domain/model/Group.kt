package com.example.domain.model

import com.google.gson.Gson

data class Group(
    val groupId: Int,
    val groupName: String,
    val groupColor: Int,
    val tasksCount: Int,
)

object GroupSerializer {
    private val gson = Gson()

    fun serialize(group: Group): String {
        return gson.toJson(group)
    }

    fun deserialize(data: String): Group {
        return gson.fromJson(data, Group::class.java)
    }
}
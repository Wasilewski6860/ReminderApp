package com.example.domain.model


data class Group(
    var groupId: Int,
    val groupName: String,
    val groupColor: Int,
    val groupImage: Int,
    val tasksCount: Int,
)

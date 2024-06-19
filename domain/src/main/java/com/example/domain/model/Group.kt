package com.example.domain.model

import java.io.Serializable

data class Group(
    var groupId: Int,
    var groupName: String,
    var groupColor: Int,
    var groupImage: Int,
    var tasksCount: Int,
) : Serializable

package com.example.domain.model

import java.io.Serializable

data class Group(
    val groupId: Int,
    val groupName: String,
    val groupColor: Int,
) : Serializable
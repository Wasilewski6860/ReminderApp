package com.example.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_group")
data class TaskGroupEntity(
    @PrimaryKey(autoGenerate = true)
    val groupId: Int,
    val groupName: String,
    val groupColor: Int,
    val groupImage: Int,
)
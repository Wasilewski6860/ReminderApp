package com.example.data.cache.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.data.cache.entity.TaskEntity
import com.example.data.cache.entity.TaskGroupEntity

data class GroupWithTasks(
    @Embedded val group: TaskGroupEntity,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId"
    )
    val tasks: List<TaskEntity>
)
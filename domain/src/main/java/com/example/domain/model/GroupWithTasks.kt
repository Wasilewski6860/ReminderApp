package com.example.domain.model


data class GroupWithTasks (
    val group: Group,
    val tasks: List<Task>
)
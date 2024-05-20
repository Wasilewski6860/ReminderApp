package com.example.domain.model


data class Task(
    val id: Int,
    val name: String,
    val description: String = "",
    val reminderCreationTime: Long,
    val reminderTimeTarget: Long,
    val type: TaskPeriodType,
    val color: Int
)
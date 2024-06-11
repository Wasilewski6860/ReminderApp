package com.example.domain.model


data class Task(
    var id: Int,
    val name: String,
    val description: String = "",
    val reminderCreationTime: Long,
    val reminderTimeTarget: Long,
    val type: TaskPeriodType,
    var isActive: Boolean,
    val color: Int
)
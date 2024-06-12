package com.example.domain.model


data class Task(
    var id: Int = 0,
    val name: String,
    val description: String = "",
    val reminderCreationTime: Long,
    val reminderTime: Long,
    val reminderTimePeriod: Long,
    val type: TaskPeriodType,
    var isActive: Boolean,
    var isMarkedWithFlag: Boolean,
    var groupId: Int,
    val color: Int
)
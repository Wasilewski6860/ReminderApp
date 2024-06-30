package com.example.domain.model

import java.io.Serializable


data class Task(
    var id: Int = 0,
    val name: String,
    val description: String = "",
    val reminderCreationTime: Long,
    val reminderTime: Long?,
    val reminderTimePeriod: Long?,
    val type: TaskPeriodType,
    var isActive: Boolean,
    var isMarkedWithFlag: Boolean,
    var groupId: Int?
): Serializable
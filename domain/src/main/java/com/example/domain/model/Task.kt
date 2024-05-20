package com.example.domain.model


data class Task(
    val id: Int,
    val name: String,
    val description: String = "",
    val timestamp: Long,
    val timeTarget: Long,
    val type: TaskPeriodType,
    val color: Int
)
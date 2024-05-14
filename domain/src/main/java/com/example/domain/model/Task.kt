package com.example.domain.model

data class Task(
    var reminderName: String,
    var reminderDescription: String = "",
    var reminderCardBackgroundColor: Int
)

// Need add time and date as args here somehow
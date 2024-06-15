package com.example.reminderapp.temppackage

import com.example.domain.model.Task
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

// TODO test method, move it to another directory
private fun getTodayReminders(): List<Task> {
    val storedTimeMillis: Long = 100 // STUB
    val storedLocalDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(storedTimeMillis), ZoneId.systemDefault()
    )

    val now = LocalDateTime.now()

    if (storedLocalDateTime.isAfter(now)) {
        // Future time
    } else if (now.toInstant(ZoneOffset.UTC).toEpochMilli() - storedTimeMillis > 24 * 60 * 60 * 1000) {
        // Past time
    } else {
        // Now
    }

    return emptyList()
}
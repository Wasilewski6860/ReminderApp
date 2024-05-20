package com.example.reminderapp.presentation.creatorscreen

import android.graphics.Color

object SpinnerColors {
    val spinnerColorsList get() = listOf(
        Color.parseColor("#808080"),
        Color.parseColor("#008000"),
        Color.parseColor("#0000CD"),
        Color.parseColor("#FFA500"),
        Color.parseColor("#FFC0CB")
    )

}

object SpinnerPeriodicTimeText {
    fun getTimesList(): List<String> {
        return listOf(
            "5 минут", "10 минут", "15 минут", "30 минут", "1 час", "2 часа"
        )
    }
}
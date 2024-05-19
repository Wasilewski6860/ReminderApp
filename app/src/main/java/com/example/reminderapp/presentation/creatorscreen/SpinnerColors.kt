package com.example.reminderapp.presentation.creatorscreen

import com.example.reminderapp.R

object SpinnerColors {
    fun getColorsList(): List<Int> {
        return listOf(
            R.color.grey, R.color.green, R.color.light_blue, R.color.orange, R.color.pink
        )
    }
}

object SpinnerPeriodicTimeText {
    fun getTimesList(): List<String> {
        return listOf(
            "5 минут", "10 минут", "15 минут", "30 минут", "1 час", "2 часа"
        )
    }
}
package com.example.reminderapp.presentation.creatorscreen

import android.content.Context
import android.graphics.Color
import com.example.reminderapp.R

object SpinnerColors {
    val spinnerColorsList
        get() = listOf(
            Color.parseColor("#808080"),
            Color.parseColor("#008000"),
            Color.parseColor("#0000CD"),
            Color.parseColor("#FFA500"),
            Color.parseColor("#FFC0CB")
        )
}

object SpinnerPeriodicTime {
    fun getTimesList(context: Context): List<String> {
        return listOf(
            context.getString(R.string.min_15),
            context.getString(R.string.min_30),
            context.getString(R.string.hour_1),
            context.getString(R.string.day_1),
            context.getString(R.string.day_2),
            context.getString(R.string.day_3),
            context.getString(R.string.week),
            context.getString(R.string.month)
        )
    }

    fun getTimesDict(context: Context): Map<Long, String> {
        return mapOf(
            900000L to context.getString(R.string.min_15),
            1800000L to context.getString(R.string.min_30),
            3600000L to context.getString(R.string.hour_1),
            86400000L to context.getString(R.string.day_1),
            172800000L to context.getString(R.string.day_2),
            259200000L to context.getString(R.string.day_3),
            604800000L to context.getString(R.string.week),
            2592000000 to context.getString(R.string.month) // month = 30 days here
        )
    }
}
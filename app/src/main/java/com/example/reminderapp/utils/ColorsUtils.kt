package com.example.reminderapp.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.reminderapp.R

class ColorsUtils(context: Context) {

    val colors: List<ColorItem> = listOf(
        ColorItem(
            "Не выбран",
            null
        ),
        ColorItem(
            "Красный",
            ContextCompat.getColor(context, R.color.red)
        ),
        ColorItem(
            "Синий",
            ContextCompat.getColor(context, R.color.blue)
        ),
        ColorItem(
            "Зеленый",
            ContextCompat.getColor(context, R.color.green)
        ),
        ColorItem(
            "Желтый",
            ContextCompat.getColor(context, R.color.yellow)
        ),
        ColorItem(
            "Фиолетовый",
            ContextCompat.getColor(context, R.color.violet)
        ),
    )
}

data class ColorItem(
    val name: String,
    val color: Int?
)
package com.example.reminderapp.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.reminderapp.R

class ColorsUtils(context: Context) {
    val colors: List<ColorItem> = listOf(
        ColorItem(
            context.getString(R.string.not_selected_color_list_item),
            null
        ),
        ColorItem(
            context.getString(R.string.red_color_list_item),
            ContextCompat.getColor(context, R.color.red)
        ),
        ColorItem(
            context.getString(R.string.blue_color_list_item),
            ContextCompat.getColor(context, R.color.blue)
        ),
        ColorItem(
            context.getString(R.string.green_color_list_item),
            ContextCompat.getColor(context, R.color.green)
        ),
        ColorItem(
            context.getString(R.string.yellow_color_list_item),
            ContextCompat.getColor(context, R.color.yellow)
        ),
        ColorItem(
            context.getString(R.string.violet_color_list_item),
            ContextCompat.getColor(context, R.color.violet)
        )
    )
}

data class ColorItem(
    val name: String,
    val color: Int?
)
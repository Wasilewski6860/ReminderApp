package com.example.reminderapp.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.reminderapp.R

class ColorsUtils(context: Context) {
    val colors: List<ColorItem> = listOf(
        ColorItem(
            context.getString(R.string.red_color_list_item),
            ContextCompat.getColor(context, R.color.red)
        ),
        ColorItem(
            context.getString(R.string.orange_color_list_item),
            ContextCompat.getColor(context, R.color.orange)
        ),
        ColorItem(
            context.getString(R.string.yellow_color_list_item),
            ContextCompat.getColor(context, R.color.yellow)
        ),
        ColorItem(
            context.getString(R.string.light_green_color_list_item),
            ContextCompat.getColor(context, R.color.light_green)
        ),
        ColorItem(
            context.getString(R.string.green_color_list_item),
            ContextCompat.getColor(context, R.color.green)
        ),
        ColorItem(
            context.getString(R.string.dark_green_color_list_item),
            ContextCompat.getColor(context, R.color.dark_green)
        ),
        ColorItem(
            context.getString(R.string.light_blue_color_list_item),
            ContextCompat.getColor(context, R.color.light_blue)
        ),
        ColorItem(
            context.getString(R.string.blue_color_list_item),
            ContextCompat.getColor(context, R.color.blue)
        ),
        ColorItem(
            context.getString(R.string.dark_blue_color_list_item),
            ContextCompat.getColor(context, R.color.dark_blue)
        ),
        ColorItem(
            context.getString(R.string.light_purple_color_list_item),
            ContextCompat.getColor(context, R.color.light_purple)
        ),
        ColorItem(
            context.getString(R.string.purple_color_list_item),
            ContextCompat.getColor(context, R.color.purple)
        ),
        ColorItem(
            context.getString(R.string.dark_purple_color_list_item),
            ContextCompat.getColor(context, R.color.dark_purple)
        ),
    )

    val onlyColors: List<ColorItem>
        get() = colors.filter { it.color != null  }

    val colorsListSize get() = colors.size
}

data class ColorItem(
    val name: String,
    val color: Int?
)
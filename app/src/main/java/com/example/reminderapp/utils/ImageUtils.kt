package com.example.reminderapp.utils

import android.content.Context
import android.media.Image
import com.example.reminderapp.R

class ImageUtils(context: Context) {
    val images: List<ImageItem> = listOf(
        ImageItem(
            "beer",
            R.drawable.beer
        ),
        ImageItem(
            "bell",
            R.drawable.bell
        ),
        ImageItem(
            "book",
            R.drawable.book
        ),
        ImageItem(
            "box",
            R.drawable.box
        ),
        ImageItem(
            "car",
            R.drawable.car
        ),
        ImageItem(
            "folder",
            R.drawable.folder
        ),
        ImageItem(
            "gamepad",
            R.drawable.gamepad
        ),
        ImageItem(
            "globe",
            R.drawable.globe
        ),
        ImageItem(
            "house",
            R.drawable.house
        ),
        ImageItem(
            "human",
            R.drawable.human
        ),
        ImageItem(
            "letter",
            R.drawable.letter
        ),
        ImageItem(
            "light_bulb",
            R.drawable.light_bulb
        ),
        ImageItem(
            "location_pin",
            R.drawable.location_pin
        ),
        ImageItem(
            "med_kit",
            R.drawable.med_kit
        ),
        ImageItem(
            "shopping_cart",
            R.drawable.shopping_cart
        ),
        ImageItem(
            "star",
            R.drawable.star
        ),
        ImageItem(
            "tea",
            R.drawable.tea
        ),
        ImageItem(
            "wrench",
            R.drawable.wrench
        )
    )

    val onlyImages: List<ImageItem>
        get() = images.filter { it.image != null }

    val imagesListSize get() = images.size
}

data class ImageItem(
    val name: String,
    val image: Int?
)
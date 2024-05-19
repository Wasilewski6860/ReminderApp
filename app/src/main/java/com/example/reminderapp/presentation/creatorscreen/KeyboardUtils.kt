package com.example.reminderapp.presentation.creatorscreen

import android.app.Activity
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.ime

object KeyboardUtils {
    fun hideKeyboard(activity: Activity, view: View) {
        WindowCompat.getInsetsController(activity.window, view).hide(ime())
    }
}
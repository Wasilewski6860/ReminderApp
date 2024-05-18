package com.example.reminderapp.presentation.creatorscreen

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        val currentFocus = activity.currentFocus
        if (currentFocus != null)
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}
package com.example.reminderapp.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(text: String, view: View) {
    Snackbar.make(
        view,
        text,
        Snackbar.LENGTH_LONG
    ).show()
}

fun View.setOnOutsideTouchListener(onOutsideTouch: () -> Unit) {
    val parent = parent as? ViewGroup
    parent?.setOnTouchListener { view, event ->
        val hitRect = Rect()
        getHitRect(hitRect)
        val isOutside = !(hitRect.contains(event.x.toInt(), event.y.toInt()))
        if (isOutside) {
            onOutsideTouch()
        }
        isOutside
    }
}
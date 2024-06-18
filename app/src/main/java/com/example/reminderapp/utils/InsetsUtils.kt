package com.example.reminderapp.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment

private fun Window.setEdgeToEdge(barColor: Int, handleEdgeToEdge: () -> Unit) {
    WindowCompat.setDecorFitsSystemWindows(this, false)

//    this.statusBarColor = barColor
//    this.navigationBarColor = barColor

    handleEdgeToEdge()
}

fun Activity.setEdgeToEdge(handleEdgeToEdge: () -> Unit) = window.setEdgeToEdge(ContextCompat.getColor(this, android.R.color.transparent), handleEdgeToEdge)

fun Fragment.setEdgeToEdge(handleEdgeToEdge: () -> Unit) = requireActivity().setEdgeToEdge(handleEdgeToEdge)

infix fun View.setPaddingToInset(insetType: Int) {
    val left = paddingLeft
    val top = paddingTop
    val right = paddingRight
    val bottom = paddingBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val inset = insets.getInsets(insetType)
        view.updatePadding(
            left = left + inset.left,
            top = top + inset.top,
            right = right + inset.right,
            bottom = bottom + inset.bottom
        )
        insets
    }
}

infix fun View.setMarginToInset(insetType: Int) {
    val left = marginLeft
    val top = marginTop
    val right = marginRight
    val bottom = marginBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val inset = insets.getInsets(insetType)
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            updateMargins(
                left = left + inset.left,
                top = top + inset.top,
                right = right + inset.right,
                bottom = bottom + inset.bottom
            )
        }
        insets
    }
}
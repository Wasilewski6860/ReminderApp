package com.example.reminderapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(text : String, view: View){
    Snackbar.make(
        view,
        text,
        Snackbar.LENGTH_LONG
    ).show()
}
package com.example.reminderapp.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.reminderapp.MainActivity
import com.google.android.material.appbar.MaterialToolbar

fun Fragment.setupToolbar(
    activity: AppCompatActivity,
    toolbar: MaterialToolbar,
    backEnable: Boolean,
    title: String? = null
) {
    activity.setSupportActionBar(toolbar)
    activity.supportActionBar?.apply {
        setDisplayShowTitleEnabled(title!=null)
        setDisplayHomeAsUpEnabled(backEnable)
    }
    if(title!=null) toolbar.title = title
}
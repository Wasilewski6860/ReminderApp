package com.example.reminderapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reminderapp.animations.playFloatingButtonAnimation
import com.example.reminderapp.databinding.ActivityMainBinding
import com.example.reminderapp.notification.Constants
import com.example.reminderapp.notification.Constants.ACTION_SHOW_TASK
import com.example.reminderapp.presentation.creatorscreen.KeyboardUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val focus = currentFocus
            if (focus is EditText) {
                val outRect = Rect()
                focus.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    KeyboardUtils.hideKeyboard(this, binding.navHostFragment)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}
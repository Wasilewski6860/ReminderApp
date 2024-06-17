package com.example.reminderapp

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import com.example.reminderapp.databinding.ActivityMainBinding
import com.example.reminderapp.utils.KeyboardUtils
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val focus = currentFocus
            if (focus is EditText) {
                val outRect = Rect()
                focus.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    KeyboardUtils.hideKeyboard(this, binding.fragmentContainerView)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setToolbarTitleAndTitleColor(title: String,color: Int? = null) {
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        toolbar.title = title
        color?.let {
            toolbar.setTitleTextColor(it)
        }
    }

}
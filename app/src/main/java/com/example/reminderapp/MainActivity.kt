package com.example.reminderapp

import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.view.WindowInsetsCompat.Type.navigationBars
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import android.widget.EditText
import androidx.core.view.WindowCompat
import com.example.reminderapp.databinding.ActivityMainBinding
import com.example.reminderapp.utils.KeyboardUtils
import com.example.reminderapp.utils.setPaddingToInset
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            toolbar setPaddingToInset statusBars()
            binding.fragmentContainerView setPaddingToInset navigationBars()
        }

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
package com.example.reminderapp

import android.graphics.Rect
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reminderapp.databinding.ActivityMainBinding
import com.example.reminderapp.notification.Constants.ACTION_SHOW_TASK
import com.example.reminderapp.presentation.creatorscreen.KeyboardUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        binding.navigationView.setupWithNavController(navHostFragment.findNavController())

        initNavigationListener(navHostFragment)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTaskIfNeeded(intent)
    }

    private fun navigateToTaskIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TASK) {
//            navHostFragment.findNavController().navigate(R.id.some_action)
        }
    }

    private fun initNavigationListener(navHostFragment: NavHostFragment) = with(binding) {
        var action = R.id.action_mainFragment_to_creatorFragment

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.mainFragment -> {
                        action = R.id.action_mainFragment_to_creatorFragment
                        floatingButton.setImageResource(R.drawable.add_icon)
                    }
                    R.id.creatorFragment -> {
                        floatingButton.setImageResource(R.drawable.check_save_icon)
                    }
                }
            }
        floatingButton.setOnClickListener {
            navHostFragment.findNavController().navigate(
                resId = action,
                args = null,
                navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_anim)
                    .build()
            )
        }
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
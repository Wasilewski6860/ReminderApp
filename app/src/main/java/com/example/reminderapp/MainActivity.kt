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

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        binding.navigationView.setupWithNavController(navHostFragment.findNavController())

        initListener(navHostFragment)

        if (intent?.action == ACTION_SHOW_TASK) {
            Log.d("MY LOG", "MainActivity navigateToTaskIfNeeded ACTION_SHOW_TASK")
            val id = intent?.extras?.getInt(Constants.TASK_ID_EXTRA)
            val args = bundleOf("taskId" to id)
            binding.navHostFragment.findNavController()
                .navigate(R.id.action_global_creatorScreen, args)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTaskIfNeeded(intent)
    }

    private fun navigateToTaskIfNeeded(intent: Intent?) {
        Log.d("MY LOG", "MainActivity navigateToTaskIfNeeded")

        if (intent?.action == ACTION_SHOW_TASK) {
            Log.d("MY LOG", "MainActivity navigateToTaskIfNeeded ACTION_SHOW_TASK")
            val id = intent.extras?.getInt(Constants.TASK_ID_EXTRA)
            val args = bundleOf("taskId" to id)
            binding.navHostFragment.findNavController()
                .navigate(R.id.action_global_creatorScreen, args)
        }
    }

    private fun initListener(navHostFragment: NavHostFragment) = with(binding) {
        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                floatingButton.setOnClickListener {
                    playFloatingButtonAnimation(floatingButton)
                    val action = R.id.action_mainFragment_to_creatorFragment
                    navHostFragment.findNavController().navigate(
                        resId = action,
                        args = null,
                        navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_anim)
                            .build()
                    )
                }
                when (destination.id) {
                    R.id.mainFragment -> {
                        floatingButton.setImageResource(R.drawable.add_icon)
                    }

                    R.id.creatorFragment -> {
                        floatingButton.setImageResource(R.drawable.check_save_icon)
                        // Hide bottom navigation view elements here
                    }

                    R.id.statisticFragment -> {
                        floatingButton.setImageResource(R.drawable.back_arrow_icon)
                    }
                }
            }

        navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.statisticFragment -> {
                    val action = R.id.action_mainFragment_to_statisticFragment
                    navHostFragment.findNavController().navigate(
                        resId = action,
                        args = null,
                        navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_anim)
                            .build()
                    )
                    true
                }

                else -> false
            }
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
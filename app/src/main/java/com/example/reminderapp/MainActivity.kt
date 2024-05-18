package com.example.reminderapp

import android.annotation.SuppressLint
import android.graphics.Rect
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.reminderapp.databinding.ActivityMainBinding
import com.example.reminderapp.presentation.SharedViewModel
import com.example.reminderapp.presentation.creatorscreen.KeyboardUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.example.reminderapp.notification.Constants.ACTION_SHOW_TASK

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var screenState: ScreenState = ScreenState.MainScreen
    private val sharedViewModel by viewModel<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.navHostFragment)

        bottomNavigationViewInitListeners()

    }

    @SuppressLint("Recycle")
    private fun bottomNavigationViewInitListeners() = with(binding) {
        floatingButton.setOnClickListener {

            val action = R.id.action_mainFragment_to_creatorFragment

            when (screenState) {
                ScreenState.MainScreen -> {
                    screenState = ScreenState.CreatorScreen
                    navController.navigate(
                        resId = action,
                        args = null,
                        navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_anim).build()
                    )
                    floatingButton.setImageResource(R.drawable.check_save_icon)
                }
                ScreenState.CreatorScreen -> {
                    sharedViewModel.isTransitionValid.observe(this@MainActivity) {
                        if (it == true) {
                            screenState = ScreenState.MainScreen
                            floatingButton.setImageResource(R.drawable.add_icon)
                        }
                    }
                    sharedViewModel.onButtonClicked()
                }
            }
            navController.navigate(R.id.creatorFragment)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTaskIfNeeded(intent)
    }

    private fun navigateToTaskIfNeeded(intent: Intent?) {
        if(intent?.action == ACTION_SHOW_TASK) {
//            navHostFragment.findNavController().navigate(R.id.some_action)
        }
        navigationView.findViewById<BottomNavigationItemView>(R.id.statisticFragment).setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val focus = currentFocus
            if (focus is EditText) {
                val outRect = Rect()
                focus.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    KeyboardUtils.hideKeyboard(this)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}

sealed class ScreenState {
    object MainScreen : ScreenState()
    object CreatorScreen : ScreenState()
}
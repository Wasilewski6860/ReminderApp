package com.example.reminderapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.reminderapp.databinding.ActivityMainBinding
import com.example.reminderapp.presentation.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        }
    }

}

sealed class ScreenState {
    object MainScreen : ScreenState()
    object CreatorScreen : ScreenState()
}
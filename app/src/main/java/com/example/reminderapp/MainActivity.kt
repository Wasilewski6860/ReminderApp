package com.example.reminderapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.reminderapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.example.reminderapp.notification.Constants.ACTION_SHOW_TASK

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.navHostFragment)

        bottomNavigationViewInitListeners()

    }

    private fun bottomNavigationViewInitListeners() = with(binding) {
        floatingButton.setOnClickListener {
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

}
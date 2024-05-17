package com.example.reminderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.reminderapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView

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
        navigationView.findViewById<BottomNavigationItemView>(R.id.statisticFragment).setOnClickListener {
            navController.popBackStack()
        }
    }

}
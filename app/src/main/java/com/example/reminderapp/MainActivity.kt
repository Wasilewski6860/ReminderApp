package com.example.reminderapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.reminderapp.databinding.ActivityMainBinding
import com.example.reminderapp.notification.Constants.ACTION_SHOW_TASK

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {



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
    }
}
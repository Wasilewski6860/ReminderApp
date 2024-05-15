package com.example.reminderapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reminderapp.databinding.ActivityMainBinding
import com.example.reminderapp.fragments.mainfragment.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainHolder, MainFragment())
                .commit()

        }
    }
}
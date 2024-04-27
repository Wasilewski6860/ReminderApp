package com.example.reminderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reminderapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

//            supportFragmentManager.beginTransaction()
//                .replace(R.id.mainHolder, TempCreatorFragment(this@MainActivity))
//                .commit()

            supportFragmentManager.beginTransaction()
                .replace(R.id.mainHolder, TempStatisticFragment(this@MainActivity))
                .commit()

        }
    }
}
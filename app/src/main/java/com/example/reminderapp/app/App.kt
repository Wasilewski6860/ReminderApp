package com.example.reminderapp.app

import android.app.Application
import android.util.Log
import com.example.reminderapp.di.appModule
import com.example.reminderapp.di.dataModule
import com.example.reminderapp.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(
                appModule, dataModule, domainModule
            ))
        }
        Log.d("Application","Application created")
    }

}
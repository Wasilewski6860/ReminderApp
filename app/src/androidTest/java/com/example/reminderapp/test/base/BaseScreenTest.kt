package com.example.reminderapp.test.base

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.reminderapp.di.dataTestModule
import com.example.reminderapp.di.domainTestModule
import com.example.reminderapp.di.presentationTestModule
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

abstract class BaseScreenTest : KoinTest, TestCase() {

    @Before
    open fun setUp() {
        stopKoin()
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            modules(
                listOf(
                    module {
                        single<Application> {
                            ApplicationProvider.getApplicationContext<Application>()
                        }
                    },
                    presentationTestModule,
                    domainTestModule,
                    dataTestModule
                )
            )
        }
//        launchActivity<MainActivity>().onActivity {
//            it.setTheme(com.example.reminderapp.R.style.Theme_ReminderApp)
//        }
    }

    @After
    fun stop() {
        stopKoin()
    }
}
package com.example.reminderapp.test

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.reminderapp.MainActivity
import com.example.reminderapp.di.appTestModule
import com.example.reminderapp.di.dataTestModule
import com.example.reminderapp.di.domainTestModule
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

abstract class BaseScreenTest : TestCase(Kaspresso.Builder.simple()), KoinTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Before
    fun setUp() {
        stopKoin()
        startKoin {
            modules(listOf(appTestModule, domainTestModule, dataTestModule))
        }
        launchActivity<MainActivity>().onActivity {
            it.setTheme(com.example.reminderapp.R.style.Theme_ReminderApp)
        }
    }

    @After
    fun stop() {
        stopKoin()
    }
}
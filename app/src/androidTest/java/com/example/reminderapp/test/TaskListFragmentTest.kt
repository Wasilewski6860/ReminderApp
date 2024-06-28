package com.example.reminderapp.test

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.screen.MainScreen
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskListFragmentTest {

    @Before
    fun setUp() {
        // Запуск временной активности перед тестом
        val scenario = FragmentScenario.launchInContainer(MainFragment::class.java)
    }

    @Test
    fun testDisplayTasks() {
        // Запуск фрагмента в контейнере
        FragmentScenario.launchInContainer(MainFragment::class.java)

        MainScreen {
            todayGridView {
                isVisible()
            }
        }
    }
}
package com.example.reminderapp.scenarios

import android.os.Bundle
import androidx.test.rule.ActivityTestRule
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

class ToMainScreenScenario(
    private val activityTestRule: ActivityTestRule<MainActivity>
) : Scenario() {

    override val steps: TestContext<Unit>.() -> Unit = {
        step("Open main screen") {
            activityTestRule.launchActivity(null)
            activityTestRule.activity.runOnUiThread {
                val fragment = MainFragment().apply {
                    arguments = Bundle().apply {
                        // Добавьте аргументы для фрагмента, если необходимо
                    }
                }
                activityTestRule.activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .commitNow()
            }
        }
    }
}

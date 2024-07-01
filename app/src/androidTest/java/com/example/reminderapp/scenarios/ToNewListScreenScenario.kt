package com.example.reminderapp.scenarios

import androidx.test.rule.ActivityTestRule
import com.example.reminderapp.MainActivity
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.screen.NewListScreen
import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

class ToNewListScreenScenario(
    private val activityTestRule: ActivityTestRule<MainActivity>
): Scenario() {

    override val steps: TestContext<Unit>.() -> Unit = {
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Open new list screen") {
            MainScreen {
                addListTv {
                    isDisplayed()
                    click()
                }
            }
        }
    }
}

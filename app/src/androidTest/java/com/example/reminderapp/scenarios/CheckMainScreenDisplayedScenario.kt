package com.example.reminderapp.scenarios

import androidx.test.rule.ActivityTestRule
import com.example.reminderapp.MainActivity
import com.example.reminderapp.screen.MainScreen
import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

class CheckMainScreenDisplayedScenario: Scenario() {

    override val steps: TestContext<Unit>.() -> Unit = {
        step("Main screen displayed") {
            MainScreen {
                todayGridView { isDisplayed() }
                plannedGridView { isVisible() }
                allGridView { isVisible() }
                withFlagGridView { isVisible() }
                noTimeGridView { isVisible() }
                mainRecycler { isVisible() }
                editListsTv { isVisible() }
                addListTv { isVisible() }
                fab { isVisible() }
            }
        }
    }
}

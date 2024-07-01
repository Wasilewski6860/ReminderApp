package com.example.reminderapp.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.reminderapp.R
import com.example.reminderapp.di.TestData
import com.example.reminderapp.scenarios.CheckMainScreenDisplayedScenario
import com.example.reminderapp.scenarios.ToNewListScreenScenario
import com.example.reminderapp.screen.NewListScreen
import com.example.reminderapp.test.base.BaseScreenTest
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class NewListScreenTest: BaseScreenTest()  {

    @Test
    fun  new_list_screen_content() = run() {
        scenario(
            ToNewListScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Checking screen displayed") {
            NewListScreen {
                flakySafely {
                    selectedColor.isDisplayed()
                    newListEditText{
                        isDisplayed()
                        hasEmptyText()
                    }
                    colorsRecycler {
                        isDisplayed()
                    }
                }

            }
        }
    }

    @Test
    fun  saving_list_test() = run() {
        scenario(
            ToNewListScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Testing of saving") {
            NewListScreen {
                newListEditText{
                    typeText("Test text")
                }
                colorsRecycler {
                    childAt<NewListScreen.ColorItem>(1){
                        click()
                    }
                }
                onView(withId(R.id.action_save)).perform(ViewActions.click())
            }
        }
        step("Checking of navigating back") {
            scenario(
                CheckMainScreenDisplayedScenario()
            )
        }
    }

}
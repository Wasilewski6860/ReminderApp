package com.example.reminderapp.test

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isFocused
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SdkSuppress
import androidx.test.rule.ActivityTestRule
import com.example.domain.model.Task
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.di.TestData
import com.example.reminderapp.scenarios.CheckMainScreenDisplayedScenario
import com.example.reminderapp.scenarios.ToMainScreenScenario
import com.example.reminderapp.screen.CreateReminderScreen
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.screen.NewListScreen
import com.example.reminderapp.screen.ReminderListScreen
import com.example.reminderapp.test.base.BaseScreenTest
import com.example.reminderapp.utils.TimeDateUtils
import io.mockk.coEvery
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject
import java.util.Locale

class CreateReminderScreenTest: BaseScreenTest()  {

    @SdkSuppress(maxSdkVersion = Build.VERSION_CODES.S)
    @Test
    fun  create_reminder_screen_content() = run() {
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Click on fab") {
            MainScreen {
                fab.click()
            }
        }
        step("Check for screen content") {
            CreateReminderScreen {
                nameEt {
                    isDisplayed()
                    hasEmptyText()
                }
                descriptionEt {
                    isDisplayed()
                    hasEmptyText()
                }
                remindTv.isDisplayed()
                remindSwitch {
                    isDisplayed()
                    isNotChecked()
                }
                flagSwitch {
                    isDisplayed()
                    isNotChecked()
                }
                selectedDateTv {
                    isNotDisplayed()
                }
                periodSpinner {
                    isNotDisplayed()
                }
            }
        }
    }

    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.S)
    @Test
    fun  open_new_list_from_create_reminder_screen() = run() {
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Click on fab") {
            MainScreen {
                fab.click()
            }
        }
        step("Clicking on create option in group spinner") {
            CreateReminderScreen {
                groupSpinner {
                    isDisplayed()
                    open()
                    onData(allOf(`is`(instanceOf(String::class.java)), `is`(context.getString(R.string.create)))).perform(click())
                }
            }
        }
        step("Creating a new group") {
            NewListScreen {
                newListEditText{
                    typeText(TestData.firstGroup.groupName)
                }
                colorsRecycler {
                    childAt<NewListScreen.ColorItem>(1){
                        click()
                    }
                }
                onView(withId(R.id.action_save)).perform(click())
            }
        }
        step("Checking of navigating back and correct selected value in spinner") {
            CreateReminderScreen {
                groupSpinner {
                    isDisplayed()
                    onView(withId(R.id.selected_list_spinner))
                        .check(
                            matches(
                                ViewMatchers.withSpinnerText(
                                    CoreMatchers.containsString(TestData.firstGroup.groupName)
                                )
                            )
                        )

                }
            }
        }
    }

    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.S)
    @Test
    fun  saving_reminder_test() = run() {
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Click on fab") {
            MainScreen {
                fab.click()
            }
        }
        step("Trying to save reminder") {
            CreateReminderScreen {
                onView(withId(R.id.action_save)).perform(click())

                step("Save unsuccessful, showing error") {
                    onView(withId(R.id.reminder_name_et))
                        .check(matches(isFocused()))
                    onView(withId(R.id.reminder_name_et))
                        .check(matches(hasErrorText(context.getString(R.string.name_shouldnt_be_empty))))
                }
                step("Input name") {
                    nameEt {
                        typeText("Test text")
                    }
                }
                step("Another try of saving") {
                    onView(withId(R.id.action_save)).perform(click())
                }
            }
        }
        step("Saving successful, check for navigation successful") {
            NewListScreen {
                newListEditText{
                    typeText(TestData.firstGroup.groupName)
                }
                colorsRecycler {
                    childAt<NewListScreen.ColorItem>(1){
                        click()
                    }
                }
                onView(withId(R.id.action_save)).perform(click())
            }
        }
        step("Checking of navigating back and correct selected value in spinner") {
            scenario(
                CheckMainScreenDisplayedScenario()
            )
        }
    }
}
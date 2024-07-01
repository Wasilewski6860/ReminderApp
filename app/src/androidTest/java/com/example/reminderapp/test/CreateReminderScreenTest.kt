package com.example.reminderapp.test

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.SdkSuppress
import androidx.test.rule.ActivityTestRule
import com.example.domain.model.Task
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.di.TestData
import com.example.reminderapp.scenarios.ToMainScreenScenario
import com.example.reminderapp.screen.CreateReminderScreen
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.screen.ReminderListScreen
import com.example.reminderapp.test.base.BaseScreenTest
import com.example.reminderapp.utils.TimeDateUtils
import io.mockk.coEvery
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject
import java.util.Locale

class CreateReminderScreenTest: BaseScreenTest()  {

    @SdkSuppress(maxSdkVersion = Build.VERSION_CODES.S)
    @Test
    fun  createReminderScreenContent() = run() {
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

}
package com.example.reminderapp.test

import android.content.Context
import androidx.appcompat.widget.AppCompatImageButton
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.scenarios.CheckMainScreenDisplayedScenario
import com.example.reminderapp.scenarios.ToEditListsScenario
import com.example.reminderapp.scenarios.ToNewListScreenScenario
import com.example.reminderapp.screen.GroupListScreen
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.screen.NewListScreen
import com.example.reminderapp.test.base.BaseScreenTest
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject

class NewListScreenTest: BaseScreenTest()  {

    @Before
    override fun setUp() {
        super.setUp()
        coEvery {taskRepository.getTasksForToday()} returns flowOf(TestData.todayTasks)
        coEvery {taskRepository.getTasksForTodayCount()} returns flowOf(TestData.todayTasks.size)
        coEvery {taskRepository.getTasksPlanned()} returns flowOf(TestData.plannedTasks)
        coEvery {taskRepository.getTasksPlannedCount()} returns flowOf(TestData.plannedTasks.size)
        coEvery {taskRepository.getTasksWithFlagCount()} returns flowOf(TestData.tasksWithFlag.size)
        coEvery{taskRepository.getCountOfNoTimeTasks()} returns flowOf(TestData.noTimeTasks.size)
        coEvery{taskRepository.getAllTasksCount()} returns flowOf(TestData.tasks.size)
        coEvery{groupRepository.getAllGroups()} returns flowOf(TestData.groups)
    }

    @Test
    fun  newListScreenContent() = run() {
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
    fun  testOfSaving() = run() {
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
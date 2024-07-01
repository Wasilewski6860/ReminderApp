package com.example.reminderapp.test

import androidx.appcompat.widget.AppCompatImageButton
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import com.example.reminderapp.R
import com.example.reminderapp.di.TestData
import com.example.reminderapp.scenarios.CheckMainScreenDisplayedScenario
import com.example.reminderapp.scenarios.ToEditListsScenario
import com.example.reminderapp.screen.GroupListScreen
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.test.base.BaseScreenTest
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Before
import org.junit.Test

class EditListsScreenTest: BaseScreenTest()  {

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
    fun  editListsScreenContent() = run() {
        scenario(
            ToEditListsScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Checking screen content") {
            GroupListScreen {
                myListsTextView {
                    isDisplayed()
                    hasText(context.getString(R.string.my_lists))
                }
                groupsRecycler {
                    isDisplayed()
                    hasSize(TestData.groups.size)
                }
            }
        }
    }

    @Test
    fun  testOfDeletion() = run() {
        scenario(
            ToEditListsScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Testing of deletion") {
            GroupListScreen {
                groupsRecycler {
                    isDisplayed()
                    hasSize(TestData.groups.size)
                    childAt<MainScreen.GroupItem>(0) {
                        trashButton.click()
                    }
                    flakySafely {
                        hasSize(TestData.groups.size -1)
                    }
                }
            }
        }
    }

    @Test
    fun  testOfPressingBack() = run() {
        scenario(
            ToEditListsScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Pressing back") {
            onView(allOf(
                instanceOf(AppCompatImageButton::class.java), withParent(withId(R.id.editListToolbar))
            ))
                .perform(click())
        }
        step("Checking is navigated back") {
            scenario(
                CheckMainScreenDisplayedScenario()
            )
        }

    }

}
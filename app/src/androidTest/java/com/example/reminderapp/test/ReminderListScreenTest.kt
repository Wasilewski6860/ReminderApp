package com.example.reminderapp.test

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.example.domain.model.Task
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.MainActivity
import com.example.reminderapp.di.TestData
import com.example.reminderapp.scenarios.CheckMainScreenDisplayedScenario
import com.example.reminderapp.scenarios.ToMainScreenScenario
import com.example.reminderapp.scenarios.ToNewListScreenScenario
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.screen.NewListScreen
import com.example.reminderapp.screen.ReminderListScreen
import com.example.reminderapp.test.base.BaseScreenTest
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject
import com.example.reminderapp.R
import com.example.reminderapp.screen.CreateReminderScreen
import com.example.reminderapp.utils.TimeDateUtils
import io.mockk.slot
import org.junit.Assert.assertTrue
import java.util.Locale

class ReminderListScreenTest: BaseScreenTest()  {

    private val timeDateUtils: TimeDateUtils by inject()
    val dateFormat = SimpleDateFormat("E, dd.MM.yyyy 'г.' HH:mm", Locale.getDefault())
    @Before
    override fun setUp() {
        super.setUp()
        coEvery { taskRepository.getTasksForToday() } returns flowOf(TestData.todayTasks)
        coEvery { taskRepository.getTasksForTodayCount() } returns flowOf(TestData.todayTasks.size)
        coEvery { taskRepository.getTasksPlanned() } returns flowOf(TestData.plannedTasks)
        coEvery { taskRepository.getTasksPlannedCount() } returns flowOf(TestData.plannedTasks.size)
        coEvery { taskRepository.getTasksWithFlagCount() } returns flowOf(TestData.tasksWithFlag.size)
        coEvery { taskRepository.getTasksWithFlag() } returns flowOf(TestData.tasksWithFlag)
        coEvery { taskRepository.getCountOfNoTimeTasks() } returns flowOf(TestData.noTimeTasks.size)
        coEvery { taskRepository.getAllTasksCount() } returns flowOf(TestData.tasks.size)
        coEvery { taskRepository.getAllTasks() } returns flowOf(TestData.tasks)
        coEvery { taskRepository.getNoTimeTasks() } returns flowOf(TestData.noTimeTasks)
        coEvery { groupRepository.getAllGroups() } returns flowOf(TestData.groups)
        val taskSlot = slot<Task>()
        coEvery { groupRepository.getGroupWithTasks(any()) } returns flowOf(TestData.groupWithTasks)
        coEvery { taskRepository.deleteTask(capture(taskSlot)) } answers {
            val deletedTask = taskSlot.captured
            coEvery { groupRepository.getGroupWithTasks(any()) } returns flowOf(TestData.groupWithTasks.copy(tasks = TestData.groupWithTasks.tasks.filter {  it != deletedTask  }))
        }
    }

    @Test
    fun  reminder_list_screen_content() = run() {
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Click on group item") {
            MainScreen {
                flakySafely {
                    mainRecycler {
                        isVisible()
                        hasSize(3)
                        firstChild<MainScreen.GroupItem> {
                            click()
                        }
                    }
                }
            }
        }
        step("Check for screen content") {
            ReminderListScreen {
                step("Toolbar has correct title") {
                    onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.reminderListToolbar))))
                        .check(matches(withText(TestData.groupWithTasks.group.groupName)))
                }
                step("Recycler display the correct data") {
                    recycler {
                        isVisible()
                        hasSize(TestData.groupWithTasks.tasks.size)
                        firstChild<ReminderListScreen.ReminderItem> {
                            nameTv {
                                isDisplayed()
                                hasText(TestData.firstTask.name)
                            }
                            descriptionTv {
                                isDisplayed()
                                hasText(TestData.firstTask.description)
                            }
                            dateTv {
                                isDisplayed()
                                hasText(timeDateUtils.getFormattedTime(TestData.firstTask.reminderTime))
                            }
                            periodTv {
                                val period = timeDateUtils.getFormattedPeriod(TestData.firstTask.reminderTimePeriod)
                                if(period!=null) {
                                    isDisplayed()
                                    hasText(period)
                                }
                            }
                            flagIv {
                                isDisplayed()
                            }
                            repeatIv {
                                isDisplayed()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun  test_of_deletion() = run() {
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Click on group item") {
            MainScreen {
                mainRecycler {
                    isVisible()
                    hasSize(3)
                    firstChild<MainScreen.GroupItem> {
                        click()
                    }
                }
            }
        }
        step("Testing of deletion") {
            ReminderListScreen {
                step("Clicking on the first item") {
                    recycler {
                        isVisible()
                        hasSize(TestData.groupWithTasks.tasks.size)
                        firstChild<ReminderListScreen.ReminderItem> {
                            deleteIv {
                                click()
                            }
                            coEvery { groupRepository.getGroupWithTasks(any()) } returns flowOf(TestData.groupWithTasks.copy(tasks = TestData.groupWithTasks.tasks.filter {  it != TestData.firstTask  }))

                        }
                    }
                }
                step("Item deleted") {
                    recycler {
                        flakySafely {
                            hasSize(TestData.groupWithTasks.tasks.size)
                        }
                    }
                }
            }
        }
    }

    @SdkSuppress(maxSdkVersion = Build.VERSION_CODES.S)
    @Test
    fun create_reminder_click_screen_open_test() = run(){
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Click on group item") {
            MainScreen {
                mainRecycler {
                    isVisible()
                    hasSize(3)
                    firstChild<MainScreen.GroupItem> {
                        click()
                    }
                }
            }
        }
        step("Testing of opening screen of creating reminder") {
            ReminderListScreen {
                step("Clicking on the first item") {
                    recycler {
                        isVisible()
                        hasSize(TestData.groupWithTasks.tasks.size)
                        firstChild<ReminderListScreen.ReminderItem> {
                            click()
                        }
                    }
                }
            }
            step("Checking a create reminder screen opened") {
                CreateReminderScreen {
                    nameEt {
                        isDisplayed()
                        hasText(TestData.firstTask.name)
                    }
                    descriptionEt {
                        isDisplayed()
                        hasText(TestData.firstTask.description)
                    }
                    remindTv.isDisplayed()
                    remindSwitch {
                        isDisplayed()
                        isChecked()
                    }
                    flagSwitch {
                        isDisplayed()
                        isChecked()
                    }
                    selectedDateTv {
                        isDisplayed()
                        hasText(dateFormat.format(TestData.firstTask.reminderTime))
                    }
                    periodSpinner {
                        isDisplayed()
                        hasText(timeDateUtils.timeDates[0].name)
                    }
                }
            }
        }
    }

}
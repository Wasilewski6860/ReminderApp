package com.example.reminderapp

import android.os.Bundle
import androidx.test.rule.ActivityTestRule
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.test.base.BaseScreenTest
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject

class MainScreenTest: BaseScreenTest() {


    @Before
    override fun setUp() {
        super.setUp()

        coEvery {taskRepository.getTasksForToday()} returns flowOf(TestData.todayTasks)
        coEvery {taskRepository.getTasksForTodayCount()}returns flowOf(TestData.todayTasks.size)
        coEvery {taskRepository.getTasksPlanned()}returns flowOf(TestData.plannedTasks)
        coEvery {taskRepository.getTasksPlannedCount()} returns flowOf(TestData.plannedTasks.size)
        coEvery {taskRepository.getTasksWithFlagCount()}returns flowOf(TestData.tasksWithFlag.size)
        coEvery{taskRepository.getCountOfNoTimeTasks()}returns flowOf(TestData.noTimeTasks.size)
        coEvery{taskRepository.getAllTasksCount()}returns flowOf(TestData.tasks.size)
        coEvery{groupRepository.getAllGroups()}returns flowOf(TestData.groups)
    }

    @Test
    fun myFragmentTest() {
        run {
            step("Launch MainActivity with MainFragment") {
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

            step("Verify UI elements") {
                MainScreen {
                    todayGridView {
                        isVisible()
                    }
                    plannedGridView { isVisible() }
                    allGridView { isVisible() }
                    withFlagGridView { isVisible() }
                    noTimeGridView { isVisible() }

                    mainRecycler {
                        isVisible()
                        hasSize(3)
                        firstChild<MainScreen.GroupItem> {
                            titleTv { hasText("Group 1") }
                            taskCountTv { hasText("2") }
                        }
                        childAt<MainScreen.GroupItem>(1) {
                            titleTv { hasText("Group 2") }
                            taskCountTv { hasText("1") }
                        }

                        childAt<MainScreen.GroupItem>(2) {
                            titleTv { hasText("Group 3") }
                            taskCountTv { hasText("0") }
                        }
                    }

                    editListsTv { isVisible() }
                    addListTv { isVisible() }
                    fab { isVisible() }
                }
            }
        }

    }

}
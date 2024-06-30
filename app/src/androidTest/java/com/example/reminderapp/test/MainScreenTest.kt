package com.example.reminderapp.test

import android.content.Context
import android.os.Bundle
import androidx.test.rule.ActivityTestRule
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.test.base.BaseScreenTest
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.screen.GroupListScreen
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.screen.NewListScreen
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject

class MainScreenTest: BaseScreenTest() {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    private val taskRepository: ITaskRepository by inject()
    private val groupRepository: IGroupRepository by inject()
    private val viewModel: MainViewModel by inject()
    private val context: Context by inject()

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

    @Test
    fun myFragmentTest() {
        run {
            step("Verify UI elements") {
                MainScreen {
                    todayGridView {
                        isDisplayed()
                    }
                    plannedGridView { isVisible() }
                    allGridView { isVisible() }
                    withFlagGridView { isVisible() }
                    noTimeGridView { isVisible() }

                    mainRecycler {
                        isVisible()
                        hasSize(3)
                        firstChild<MainScreen.GroupItem> {
                            titleTv { hasText(TestData.firstGroup.groupName) }
                            taskCountTv { hasText(TestData.firstGroup.tasksCount.toString()) }
                        }
                        childAt<MainScreen.GroupItem>(1) {
                            titleTv { hasText(TestData.secondGroup.groupName) }
                            taskCountTv { hasText(TestData.secondGroup.tasksCount.toString()) }
                        }

                        childAt<MainScreen.GroupItem>(2) {
                            titleTv { hasText(TestData.thirdGroup.groupName) }
                            taskCountTv { hasText(TestData.thirdGroup.tasksCount.toString()) }
                        }
                    }

                    editListsTv { isVisible() }
                    addListTv { isVisible() }
                    fab { isVisible() }
                }
            }
        }
    }

    @Test
    fun  editListsScreenOpensOnEditListsClick() = run(){
        step("Clicking on edit lists button") {
            MainScreen {
                editListsTv {
                    isDisplayed()
                    click()
                }
            }
        }
        step("Checking is EditListsScreen displayed") {
            GroupListScreen {
                myListsTextView {
                    isDisplayed()
                }
                groupsRecycler {
                    isDisplayed()
                }
            }
        }
    }

    @Test
    fun  newListScreenOpensOnNewListClick() = run(){
        step("Clicking on new list button") {
            MainScreen {
                addListTv {
                    isDisplayed()
                    click()
                }
            }
        }
        step("Checking is EditListsScreen displayed") {
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

}
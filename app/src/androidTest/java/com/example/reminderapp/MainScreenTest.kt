package com.example.reminderapp

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.domain.use_case.group.GetAllGroupsUseCase
import com.example.domain.use_case.task.GetAllTasksCountUseCase
import com.example.domain.use_case.task.GetNoTimeTasksCountUseCase
import com.example.domain.use_case.task.GetTasksForTodayCountUseCase
import com.example.domain.use_case.task.GetTasksPlannedCountUseCase
import com.example.domain.use_case.task.GetTasksWithFlagCountUseCase
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.screen.MainScreen
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.BaseTestCase
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KTextView
import io.mockk.coEvery
import io.mockk.mockk
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
                    todayGridView { isVisible() }
                    plannedGridView { isVisible() }
                    allGridView { isVisible() }
                    withFlagGridView { isVisible() }
                    noTimeGridView { isVisible() }

//                    mainRecycler {
//                        isVisible()
//                        firstChild<MainScreen.GroupItem> {
//                            titleTv { hasText("Mocked Title") }
//                        }
//                    }

                    editListsTv { isVisible() }
                    addListTv { isVisible() }
                    fab { isVisible() }
                }
            }
        }

    }

}
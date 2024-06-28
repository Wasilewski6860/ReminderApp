package com.example.reminderapp.test

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.cache.TaskDao
import com.example.data.cache.TaskStorage
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.domain.use_case.group.GetAllGroupsUseCase
import com.example.domain.use_case.task.GetAllTasksCountUseCase
import com.example.domain.use_case.task.GetNoTimeTasksCountUseCase
import com.example.domain.use_case.task.GetTasksForTodayCountUseCase
import com.example.domain.use_case.task.GetTasksPlannedCountUseCase
import com.example.domain.use_case.task.GetTasksWithFlagCountUseCase
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.di.TestData
import com.example.reminderapp.di.appTestModule
import com.example.reminderapp.di.dataTestModule
import com.example.reminderapp.di.domainTestModule
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.view.KGridItemView
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.mock


class MainScreenTest : BaseScreenTest() {

    private val taskRepository: ITaskRepository by inject()
    private val groupRepository: IGroupRepository by inject()
    private val viewModel: MainViewModel by inject()

    @Test
    fun screen_content_test() {
        coEvery {taskRepository.getTasksForToday()} returns flowOf(TestData.todayTasks)
        coEvery {taskRepository.getTasksForTodayCount()}returns flowOf(TestData.todayTasks.size)
        coEvery {taskRepository.getTasksPlanned()}returns flowOf(TestData.plannedTasks)
        coEvery {taskRepository.getTasksPlannedCount()} returns flowOf(TestData.plannedTasks.size)
        coEvery {taskRepository.getTasksWithFlagCount()}returns flowOf(TestData.tasksWithFlag.size)
        coEvery{taskRepository.getCountOfNoTimeTasks()}returns flowOf(TestData.noTimeTasks.size)
        coEvery{taskRepository.getAllTasksCount()}returns flowOf(TestData.tasks.size)
        coEvery{groupRepository.getAllGroups()}returns flowOf(TestData.groups)

        launchFragmentInContainer<MainFragment>(
            themeResId = R.style.Theme_ReminderApp
        )
//        viewModel.fetchData(Unit)

        run {
            step("Screen content check") {
                flakySafely(timeoutMs = 3000) {
                    MainScreen {
                        step("Check toolbar content") {
//                            toolbar {
//                                isVisible()
//                            }
                        }
                        step("Categories grid content") {
                            todayGridView {
                                isVisible()
//                                counterTextView{
//                                    isVisible()
//                                    hasText(TestData.todayTasks.size.toString())
//                                }
//                                titleTextView.isVisible()
//                                titleTextView.hasText(R.string.current_day)
                            }
                            plannedGridView {
                                isVisible()
                            }
                            allGridView {
                                isVisible()
                            }
                            withFlagGridView {
                                isVisible()
                            }
                            noTimeGridView {
                                isVisible()
                            }
                        }
                        step("Group recycler content") {
                            mainRecycler {
                                isVisible()
                            }
                        }
                        step("Controls content") {
                            editListsTv {
                                isVisible()
                            }
                            fab {
                                isVisible()
                            }
                            addListTv {
                                isVisible()
                            }
                        }
                    }
                }
            }
        }
    }
}

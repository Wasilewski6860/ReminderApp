package com.example.reminderapp.test

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.R
import com.example.reminderapp.di.TestData
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.screen.MainScreen
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class MainEspressoScreenTest : BaseEspressoTest() {

    private val taskRepository: ITaskRepository by inject()
    private val groupRepository: IGroupRepository by inject()
    private val viewModel: MainViewModel by inject()

    @Test
    fun screen_content_test() {
        coEvery {taskRepository.getTasksForToday()} returns flowOf(TestData.todayTasks)
        coEvery {taskRepository.getTasksForTodayCount()} returns flowOf(TestData.todayTasks.size)
        coEvery {taskRepository.getTasksPlanned()} returns flowOf(TestData.plannedTasks)
        coEvery {taskRepository.getTasksPlannedCount()} returns flowOf(TestData.plannedTasks.size)
        coEvery {taskRepository.getTasksWithFlagCount()} returns flowOf(TestData.tasksWithFlag.size)
        coEvery{taskRepository.getCountOfNoTimeTasks()} returns flowOf(TestData.noTimeTasks.size)
        coEvery{taskRepository.getAllTasksCount()} returns flowOf(TestData.tasks.size)
        coEvery{groupRepository.getAllGroups()} returns flowOf(TestData.groups)

        // Запускаем фрагмент с использованием FragmentScenario внутри активити
        activityScenarioRule.scenario.onActivity { activity ->
            // Убедитесь, что фрагмент запущен на основном потоке, но внутри активности
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, MainFragment())
                .commitNow()
        }

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

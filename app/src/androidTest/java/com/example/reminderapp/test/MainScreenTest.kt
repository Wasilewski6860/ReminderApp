package com.example.reminderapp.test

import com.example.reminderapp.test.base.BaseScreenTest
import com.example.reminderapp.di.TestData
import com.example.reminderapp.scenarios.ToMainScreenScenario
import com.example.reminderapp.screen.GroupListScreen
import com.example.reminderapp.screen.MainScreen
import com.example.reminderapp.screen.NewListScreen
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class MainScreenTest: BaseScreenTest() {

    @Test
    fun main_screen_content_test() = run {
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Check for screen content") {
            MainScreen {
                step("Categories displayed") {
                    todayGridView {
                        isDisplayed()
//                        imageView.hasDrawable(com.example.reminderapp.R.drawable.sand_clock_icon)
                    }
                    plannedGridView { isDisplayed() }
                    allGridView { isDisplayed() }
                    withFlagGridView { isDisplayed() }
                    noTimeGridView { isDisplayed() }
                }
                step("Group list displayed") {
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
                }
                step("Bottom buttons displayed") {
                    step("Edit lists button displayed") {
                        editListsTv { isVisible() }
                    }
                    step("Add list button displayed") {
                        addListTv { isVisible() }
                    }
                    step("Add reminder button displayed") {
                        fab { isVisible() }
                    }
                }
            }
        }
    }


    @Test
    fun  edit_lists_screen_opens_on_edit_lists_click() = run() {
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
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
    fun  new_list_screen_opens_on_new_list_click() = run() {
        scenario(
            ToMainScreenScenario(
                activityTestRule = activityTestRule
            )
        )
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
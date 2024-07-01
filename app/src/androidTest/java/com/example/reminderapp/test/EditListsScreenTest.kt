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

    @Test
    fun  edit_lists_screen_content() = run() {
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
                    firstChild<MainScreen.GroupItem> {
                        trashButton {
                            isDisplayed()
                            hasDrawable(R.drawable.trash_delete_icon)
                        }
                        titleTv {
                            isDisplayed()
                            hasText(TestData.firstGroup.groupName)
                        }
                        taskCountTv {
                            isDisplayed()
                            hasText(TestData.firstGroup.tasksCount.toString())
                        }
                    }
                }

            }
        }
    }

    @Test
    fun  test_of_deletion() = run() {
        scenario(
            ToEditListsScenario(
                activityTestRule = activityTestRule
            )
        )
        step("Testing of deletion") {
            GroupListScreen {
                groupsRecycler {
                    step("List has correct size") {
                        isDisplayed()
                        hasSize(TestData.groups.size)
                    }
                    step("Click to delete item") {
                        childAt<MainScreen.GroupItem>(0) {
                            trashButton.click()
                        }
                    }
                    step("Item deleted") {
                        flakySafely {
                            hasSize(TestData.groups.size -1)
                        }
                    }
                }
            }
        }
    }

    @Test
    fun  test_of_pressing_back() = run() {
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
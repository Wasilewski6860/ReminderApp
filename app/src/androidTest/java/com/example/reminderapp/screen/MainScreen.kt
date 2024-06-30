package com.example.reminderapp.screen

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.reminderapp.R
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.view.KGridItemView
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar
import org.hamcrest.Matcher

object MainScreen : KScreen<MainScreen>() {
    override val layoutId: Int = R.layout.main_screen
    override val viewClass: Class<*> = MainFragment::class.java
    //    val toolbar = KToolbar { withId(R.id.main_toolbar) }
    val todayGridView = KGridItemView { withId(R.id.currentDayTasksItem) }
    val plannedGridView = KGridItemView { withId(R.id.plannedTasksItem) }
    val allGridView = KGridItemView { withId(R.id.allTasksItem) }
    val withFlagGridView = KGridItemView { withId(R.id.tasksWithFlagItem) }
    val noTimeGridView = KGridItemView { withId(R.id.tasksNoTimeItem) }

    val mainRecycler = KRecyclerView(
        builder = { withId(R.id.customListsRecyclerView) },
        itemTypeBuilder = { itemType(MainScreen::GroupItem) }
    )

    internal class GroupItem(parent: Matcher<View>) : KRecyclerItem<GroupItem>(parent) {
        val trashButton: KImageView = KImageView(parent) { withId(R.id.trashImageView) }
        val titleTv: KTextView = KTextView(parent) { withId(R.id.listNameTextView) }
        val taskCountTv: KTextView = KTextView(parent) { withId(R.id.listItemsCounterTextView) }
    }

    val editListsTv = KTextView { withId(R.id.changeListsButton) }
    val addListTv = KTextView { withId(R.id.addListButton) }
    val fab = KView { withId(R.id.addTaskButton) }
}
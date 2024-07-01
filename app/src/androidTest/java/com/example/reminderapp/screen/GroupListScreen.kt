package com.example.reminderapp.screen

import android.view.View
import com.example.reminderapp.R
import com.example.reminderapp.presentation.editorlistsscreen.EditListsScreenFragment
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.view.KGridItemView
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar
import org.hamcrest.Matcher

object GroupListScreen : KScreen<GroupListScreen>() {
    override val layoutId: Int = R.layout.list_editor_screen
    override val viewClass: Class<*> = EditListsScreenFragment::class.java

    val groupsRecycler = KRecyclerView(
        builder = { withId(R.id.editListsRecyclerView) },
        itemTypeBuilder = { itemType(MainScreen::GroupItem) }
    )
    val myListsTextView = KTextView { withId(R.id.myListsTextView) }

    val toolbar = KToolbar { withId(R.id.editListToolbar) }
}
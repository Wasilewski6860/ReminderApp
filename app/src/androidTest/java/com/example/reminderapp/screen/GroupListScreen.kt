package com.example.reminderapp.screen

import com.example.reminderapp.R
import com.example.reminderapp.presentation.editorlistsscreen.EditListsFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar

object GroupListScreen : KScreen<GroupListScreen>() {
    override val layoutId: Int = R.layout.list_editor_screen
    override val viewClass: Class<*> = EditListsFragment::class.java

    val groupsRecycler = KRecyclerView(
        builder = { withId(R.id.editListsRecyclerView) },
        itemTypeBuilder = { itemType(MainScreen::GroupItem) }
    )
    val myListsTextView = KTextView { withId(R.id.myListsTextView) }

    val toolbar = KToolbar { withId(R.id.editListToolbar) }
}
package com.example.reminderapp.screen

import android.view.View
import com.example.reminderapp.R
import com.example.reminderapp.presentation.new_list.NewListFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.toolbar.KToolbar
import org.hamcrest.Matcher

object NewListScreen : KScreen<NewListScreen>() {
    override val layoutId: Int = R.layout.fragment_new_list
    override val viewClass: Class<*> = NewListFragment::class.java

    val colorsRecycler = KRecyclerView(
        builder = { withId(R.id.colors_rv) },
        itemTypeBuilder = { itemType(NewListScreen::ColorItem) }
    )

    internal class ColorItem(parent: Matcher<View>) : KRecyclerItem<ColorItem>(parent) {
        val colorView: KView = KView(parent) { withId(R.id.colorCircleItem) }
    }

    val selectedColor = KView { withId(R.id.selected_color_iv) }
    val newListEditText = KEditText { withId(R.id.new_list_et) }

    val toolbar = KToolbar { withId(R.id.createListToolbar) }
}
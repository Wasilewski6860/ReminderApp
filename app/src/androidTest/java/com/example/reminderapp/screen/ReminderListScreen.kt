package com.example.reminderapp.screen

import android.view.View
import com.example.reminderapp.R
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.presentation.reminder_list.ReminderListFragment
import com.example.reminderapp.view.KGridItemView
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.switch.KSwitch
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar
import org.hamcrest.Matcher

object ReminderListScreen : KScreen<ReminderListScreen>() {
    override val layoutId: Int = R.layout.fragment_reminder_list
    override val viewClass: Class<*> = ReminderListFragment::class.java

    val recycler = KRecyclerView(
        builder = { withId(R.id.recyclerView) },
        itemTypeBuilder = { itemType(ReminderListScreen::ReminderItem) }
    )

    internal class ReminderItem(parent: Matcher<View>) : KRecyclerItem<ReminderItem>(parent) {
        val trashButton: KSwitch = KSwitch(parent) { withId(R.id.switchIsActive) }
        val nameTv: KTextView = KTextView(parent) { withId(R.id.reminder_name_tv) }
        val descriptionTv: KTextView = KTextView(parent) { withId(R.id.reminder_description_tv) }
        val dateTv: KTextView = KTextView(parent) { withId(R.id.reminder_date_tv) }
        val periodTv: KTextView = KTextView(parent) { withId(R.id.reminder_period_tv) }
        val flagIv: KImageView = KImageView(parent) { withId(R.id.reminder_flag_iv) }
        val repeatIv: KImageView = KImageView(parent) { withId(R.id.reminder_repeat_iv) }
        val deleteIv: KImageView = KImageView(parent) { withId(R.id.reminder_delete_iv) }
    }

    val fab = KView { withId(R.id.addFloatingActionButton) }
    val toolbar = KToolbar { withId(R.id.reminderListToolbar) }
}
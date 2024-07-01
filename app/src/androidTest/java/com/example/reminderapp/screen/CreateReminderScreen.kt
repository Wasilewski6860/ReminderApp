package com.example.reminderapp.screen

import android.view.View
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.reminderapp.R
import com.example.reminderapp.presentation.create_reminder.CreateReminderFragment
import com.example.reminderapp.presentation.reminder_list.ReminderListFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.list.KAdapterItem
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.spinner.KSpinner
import io.github.kakaocup.kakao.switch.KSwitch
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar
import org.hamcrest.Matcher

object CreateReminderScreen : KScreen<CreateReminderScreen>() {
    override val layoutId: Int = R.layout.fragment_create_reminder
    override val viewClass: Class<*> = CreateReminderFragment::class.java

    val periodSpinner = KSpinner({
        withId(R.id.selected_period_spinner)
    }) { itemType(CreateReminderScreen::SpinnerItem) }

    val groupSpinner = KSpinner({
        withId(R.id.selected_list_spinner)
    }) { itemType(CreateReminderScreen::SpinnerItem) }

    internal class SpinnerItem(parent: DataInteraction) : KAdapterItem<SpinnerItem>(parent) {
        val textView = KTextView(parent) { withId(android.R.id.text1) }
    }

    val remindTv = KTextView { withId(R.id.remind_tv)}
    val nameEt = KEditText { withId(R.id.reminder_name_et)}
    val descriptionEt = KEditText { withId(R.id.reminder_description_et)}
    val remindSwitch = KSwitch { withId(R.id.remind_switch)}
    val flagSwitch = KSwitch { withId(R.id.flag_switch)}
    val selectedDateTv = KTextView { withId(R.id.selected_date_tv)}
    val toolbar = KToolbar { withId(R.id.createToolbar) }
}
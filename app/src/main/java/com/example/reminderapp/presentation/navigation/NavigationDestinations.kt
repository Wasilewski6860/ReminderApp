package com.example.reminderapp.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.reminderapp.presentation.create_reminder.CreateReminderFragment
import com.example.reminderapp.presentation.editorlistsscreen.EditListsScreenFragment
import com.example.reminderapp.presentation.new_list.NewListFragment
import com.example.reminderapp.presentation.reminder_list.ReminderListFragment
import kotlin.reflect.KClass

sealed class NavigationDestinations(val fragmentClass: KClass<out Fragment>) {

    object ToCreateReminderFragment : NavigationDestinations(CreateReminderFragment::class)
    object ToEditListsFragment : NavigationDestinations(EditListsScreenFragment::class)
    object ToNewListFragment : NavigationDestinations(NewListFragment::class)
    object ToReminderListFragment : NavigationDestinations(ReminderListFragment::class)

}
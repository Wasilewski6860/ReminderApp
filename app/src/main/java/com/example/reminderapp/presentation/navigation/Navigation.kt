package com.example.reminderapp.presentation.navigation

sealed class Navigation {

    object ToCreateReminderFragment : Navigation()

    object ToEditListsFragment : Navigation()

    object ToNewListFragment : Navigation()

    object ToReminderListFragment : Navigation()

}
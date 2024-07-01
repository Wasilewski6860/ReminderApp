package com.example.reminderapp.presentation.base

import android.os.Bundle
import com.example.reminderapp.navigation.NavigationDestinations

abstract class NavigationFragment: BaseFragment() {

    abstract val backstackTag: String
    fun navigateTo(destination: NavigationDestinations, args: Bundle? = null) {
        navigationManager.navigateTo(
            fragment = destination.fragmentClass.java.newInstance(),
            args = args,
            backStack = backstackTag
            )
    }
}
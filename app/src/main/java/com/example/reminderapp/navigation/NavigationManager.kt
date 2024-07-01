package com.example.reminderapp.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.reminderapp.R

class NavigationManager(private val fragmentManager: FragmentManager) {

    fun navigateTo(
        fragment: Fragment,
        container: Int = R.id.fragmentContainerView,
        backStack: String,
        enterAnim: Int = R.anim.slide_in_anim,
        exitAnim: Int = R.anim.slide_out_anim,
        popEnterAnim: Int = R.anim.slide_in_anim,
        popExitAnim: Int = R.anim.slide_out_anim,
        args: Bundle? = null
    ) {
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                enterAnim,
                exitAnim,
                popEnterAnim,
                popExitAnim
            )
            .replace(
                container,
                fragment.apply {
                    args?.let {
                        arguments = it
                    }
                }
            )
            .addToBackStack(backStack)
            .commit()
    }

    fun navigateBack() {
        fragmentManager.popBackStack()
    }
}

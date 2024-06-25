package com.example.reminderapp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.reminderapp.presentation.navigation.NavigationManager

abstract class BaseFragment: Fragment(), BackActionHandler {

    protected lateinit var navigationManager: NavigationManager

    private var callback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationManager = NavigationManager(parentFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupOnBackPressed()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback?.remove()
    }

    fun setupOnBackPressed() {
        if (callback == null) {
            callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateBack()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback!!)
        }
    }

    override fun navigateBack() {
        navigationManager.navigateBack()
    }
}
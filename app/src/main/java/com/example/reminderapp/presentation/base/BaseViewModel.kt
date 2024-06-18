package com.example.reminderapp.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<In, Out> : ViewModel() {

    abstract val uiState: StateFlow<UiState<Out>>

    abstract fun fetchData(params: In)
}
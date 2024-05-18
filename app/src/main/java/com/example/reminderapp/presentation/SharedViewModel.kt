package com.example.reminderapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _buttonClicked = MutableLiveData<Boolean>()
    val buttonClicked: LiveData<Boolean> get() = _buttonClicked

    private val _isTransitionValid = MutableLiveData<Boolean>()
    val isTransitionValid: LiveData<Boolean> get() = _isTransitionValid

    fun onButtonClicked() {
        _buttonClicked.value = true
    }

    fun passTransition() {
        _isTransitionValid.value = true
    }

    fun onClickSuccess() {
        _buttonClicked.value = false
        _isTransitionValid.value = false
    }

}
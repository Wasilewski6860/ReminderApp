package com.example.reminderapp.presentation.base

import android.os.Bundle

abstract class BaseDataReceiveFragment: BaseFragment(), DataReceiver {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveData()
    }
}
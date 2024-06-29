package com.example.reminderapp.app

import com.example.reminderapp.di.appTestModule
import com.example.reminderapp.di.dataTestModule
import com.example.reminderapp.di.domainTestModule
import org.koin.core.module.Module

class TestApp: App() {
    override fun createModules(): List<Module> = listOf(appTestModule, domainTestModule, dataTestModule)
}
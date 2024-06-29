package com.example.reminderapp.di

import com.example.domain.alarm.IRemindAlarmManager
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import io.mockk.mockk
import org.koin.dsl.module

val dataTestModule = module {
    single<ITaskRepository> {
        mockk(relaxed = true)
    }

    single<IGroupRepository> {
        mockk(relaxed = true)
    }

    single<IRemindAlarmManager> {
        mockk(relaxed = true)
    }
}
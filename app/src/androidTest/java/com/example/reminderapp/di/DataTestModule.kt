package com.example.reminderapp.di

import com.example.data.repositories.GroupRepositoryImpl
import com.example.data.repositories.TaskRepositoryImpl
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.nhaarman.mockitokotlin2.mock
import io.mockk.mockk
import org.koin.dsl.module
import org.mockito.Mockito.mock

val dataTestModule = module {
    single<ITaskRepository> {
        mockk(relaxed = true)
    }

    single<IGroupRepository> {
        mockk(relaxed = true)
    }
}
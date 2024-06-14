package com.example.reminderapp.di

import com.example.data.cache.GroupCacheMapper
import com.example.data.cache.GroupWithTasksCacheMapper
import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDatabase
import com.example.data.cache.TaskStorage
import com.example.data.cache.impl.TaskStorageImpl
import com.example.data.repositories.TaskRepositoryImpl
import com.example.domain.repository.TaskRepository
import org.koin.dsl.module

val dataModule = module {

    single<TaskDatabase> {
        TaskDatabase.getDataBase(context = get())
    }

    single<TaskStorage> {
        TaskStorageImpl(
            taskCacheMapper = get(),
            taskDatabase = get(),
            groupWithTasksCacheMapper = get(),
            groupCacheMapper = get()
        )
    }

    single<TaskRepository> {
        TaskRepositoryImpl(taskStorage = get())
    }

    single<TaskCacheMapper> {
        TaskCacheMapper()
    }

    single<GroupWithTasksCacheMapper> {
        GroupWithTasksCacheMapper(get(), get())
    }

    single<GroupCacheMapper> {
        GroupCacheMapper()
    }

}
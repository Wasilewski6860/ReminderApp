package com.example.reminderapp.di

import com.example.data.cache.GroupCacheMapper
import com.example.data.cache.GroupWithTasksCacheMapper
import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDao
import com.example.data.cache.TaskDatabase
import com.example.data.cache.TaskStorage
import com.example.data.cache.impl.TaskStorageImpl
import com.example.data.repositories.GroupRepositoryImpl
import com.example.data.repositories.TaskRepositoryImpl
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import com.example.reminderapp.remind.receivers.ReminderNotificationBroadcastReceiver
import org.koin.dsl.module

val dataModule = module {

    single<TaskDatabase> {
        TaskDatabase.getDataBase(context = get())
    }

    single<TaskDao> {
        val database = get<TaskDatabase>()
        database.getTaskDao()
    }

    single<TaskStorage> {
        TaskStorageImpl(
            taskCacheMapper = get(),
            taskDao = get(),
            groupWithTasksCacheMapper = get(),
            groupCacheMapper = get()
        )
    }

    single<ITaskRepository> {
        TaskRepositoryImpl(taskStorage = get())
    }

    single<IGroupRepository> {
        GroupRepositoryImpl(taskStorage = get())
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
package com.example.reminderapp.di

import android.content.Context
import com.example.data.cache.GroupCacheMapper
import com.example.data.cache.GroupWithTasksCacheMapper
import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDao
import com.example.data.cache.TaskDatabase
import com.example.data.cache.TaskStorage
import com.example.data.cache.impl.TaskStorageImpl
import com.example.data.repositories.TaskRepositoryImpl
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository
import com.example.reminderapp.R
import com.example.reminderapp.remind.receivers.ReminderBroadcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.dsl.module
import kotlin.math.sign

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

    single<TaskRepository> {
        TaskRepositoryImpl(taskStorage = get(), context = get(), reminderReceiverClass = ReminderBroadcast::class.java)
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
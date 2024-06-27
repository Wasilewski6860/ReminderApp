package com.example.data.di

import com.example.data.cache.GroupCacheMapper
import com.example.data.cache.GroupWithTasksCacheMapper
import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDao
import com.example.data.cache.TaskStorage
import com.example.data.cache.impl.TaskStorageImpl
import com.example.domain.repository.ITaskRepository
import io.mockk.mockk
import org.koin.dsl.module

val dataTestModule = module {
    single<TaskDao> { mockk() }
    single<ITaskRepository> { mockk() }
    single<TaskStorage> { TaskStorageImpl(get(),get(), get(), get()) }

    single { TaskCacheMapper() }
    single { GroupWithTasksCacheMapper(get(), get()) }
    single { GroupCacheMapper() }
}

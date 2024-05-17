package com.example.reminderapp.di

import com.example.data.Mapper
import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDatabase
import com.example.data.cache.TaskStorage
import com.example.data.cache.entity.TaskEntity
import com.example.data.cache.impl.TaskStorageImpl
import com.example.data.repositories.TaskRepositoryImpl
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import org.koin.dsl.module

val dataModule = module {

    single<TaskDatabase> {
        TaskDatabase.getDataBase(context = get())
    }

    single<TaskStorage> {
        TaskStorageImpl( mapper = get(), taskDatabase = get())
    }

    single<TaskRepository> {
        TaskRepositoryImpl(taskStorage = get())
    }

    single<TaskCacheMapper> {
        TaskCacheMapper()
    }

}
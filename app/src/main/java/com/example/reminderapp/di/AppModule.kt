package com.example.reminderapp.di

import com.example.reminderapp.presentation.creatorscreen.CreatorViewModel
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.work.RemindWorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<RemindWorkManager> {
        RemindWorkManager(androidContext())
    }

    viewModel<MainViewModel> {
        MainViewModel(
            getAllTasksUseCase = get(),
            deleteTaskUseCase = get()
        )
    }

    viewModel<CreatorViewModel> {
        CreatorViewModel(
            saveTaskUseCase = get(),
            getTaskUseCase = get()
        )
    }

}
package com.example.reminderapp.di

import com.example.reminderapp.viewmodels.creatorscreen.CreatorViewModel
import com.example.reminderapp.viewmodels.mainscreen.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

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
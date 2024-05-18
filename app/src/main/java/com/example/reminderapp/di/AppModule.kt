package com.example.reminderapp.di

import com.example.reminderapp.presentation.SharedViewModel
import com.example.reminderapp.presentation.creatorscreen.CreatorViewModel
import com.example.reminderapp.presentation.mainscreen.MainViewModel
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

    viewModel<SharedViewModel> {
        SharedViewModel()
    }

}
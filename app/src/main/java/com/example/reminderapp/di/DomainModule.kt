package com.example.reminderapp.di

import com.example.domain.use_case.ClearAllTasksUseCase
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.GetAllOneTimeTasksUseCase
import com.example.domain.use_case.GetAllPeriodicTasksUseCase
import com.example.domain.use_case.GetAllTasksUseCase
import com.example.domain.use_case.GetTaskUseCase
import com.example.domain.use_case.SaveTaskUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<ClearAllTasksUseCase> {
        ClearAllTasksUseCase(taskRepository = get())
    }

    factory<DeleteTaskUseCase> {
        DeleteTaskUseCase(taskRepository = get())
    }

    factory<GetAllOneTimeTasksUseCase> {
        GetAllOneTimeTasksUseCase(taskRepository = get())
    }

    factory<GetAllPeriodicTasksUseCase> {
        GetAllPeriodicTasksUseCase(taskRepository = get())
    }

    factory<GetAllTasksUseCase> {
        GetAllTasksUseCase(taskRepository = get())
    }

    factory<GetTaskUseCase> {
        GetTaskUseCase(taskRepository = get())
    }

    factory<SaveTaskUseCase> {
        SaveTaskUseCase(taskRepository = get())
    }

}
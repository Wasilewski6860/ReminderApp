package com.example.reminderapp.di

import com.example.domain.use_case.ClearAllTasksUseCase
import com.example.domain.use_case.DeleteGroupUseCase
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.EditTaskUseCase
import com.example.domain.use_case.GetAllGroupsUseCase
import com.example.domain.use_case.GetAllOneTimeTasksUseCase
import com.example.domain.use_case.GetAllPeriodicTasksUseCase
import com.example.domain.use_case.GetAllTasksCountUseCase
import com.example.domain.use_case.GetAllTasksUseCase
import com.example.domain.use_case.GetGroupUseCase
import com.example.domain.use_case.GetGroupWithTasksUseCase
import com.example.domain.use_case.GetPlannedTasksBaseUseCase
import com.example.domain.use_case.GetPlannedTasksUseCase
import com.example.domain.use_case.GetTaskUseCase
import com.example.domain.use_case.GetTasksForTodayCountUseCase
import com.example.domain.use_case.GetTasksForTodayUseCase
import com.example.domain.use_case.GetTasksPlannedCountUseCase
import com.example.domain.use_case.GetTasksWithFlagCountUseCase
import com.example.domain.use_case.GetTasksWithFlagUseCase
import com.example.domain.use_case.InsertGroupUseCase
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

    factory<EditTaskUseCase> {
        EditTaskUseCase(taskRepository = get())
    }

    factory<GetAllGroupsUseCase> {
        GetAllGroupsUseCase(taskRepository = get())
    }

    factory<GetGroupUseCase> {
        GetGroupUseCase(taskRepository = get())
    }

    factory<GetGroupWithTasksUseCase> {
        GetGroupWithTasksUseCase(taskRepository = get())
    }

    factory<DeleteGroupUseCase> {
        DeleteGroupUseCase(taskRepository = get())
    }

    factory<GetPlannedTasksUseCase> {
        GetPlannedTasksUseCase(taskRepository = get())
    }

    factory<GetTasksPlannedCountUseCase> {
        GetTasksPlannedCountUseCase(taskRepository = get())
    }

    factory<GetTasksForTodayUseCase> {
        GetTasksForTodayUseCase(taskRepository = get())
    }

    factory<GetTasksForTodayCountUseCase> {
        GetTasksForTodayCountUseCase(taskRepository = get())
    }

    factory<GetTasksWithFlagUseCase> {
        GetTasksWithFlagUseCase(taskRepository = get())
    }

    factory<GetTasksWithFlagCountUseCase> {
        GetTasksWithFlagCountUseCase(taskRepository = get())
    }

    factory<InsertGroupUseCase> {
        InsertGroupUseCase(taskRepository = get())
    }

    factory<GetAllTasksCountUseCase> {
        GetAllTasksCountUseCase(taskRepository = get())
    }

}
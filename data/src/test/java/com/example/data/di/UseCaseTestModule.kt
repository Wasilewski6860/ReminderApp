package com.example.data.di

import com.example.domain.repository.TaskRepository
import com.example.domain.use_case.ClearAllTasksUseCase
import com.example.domain.use_case.DeleteGroupUseCase
import com.example.domain.use_case.DeleteTaskUseCase
import com.example.domain.use_case.EditGroupUseCase
import com.example.domain.use_case.EditTaskUseCase
import com.example.domain.use_case.GetAllGroupsUseCase
import com.example.domain.use_case.GetAllOneTimeTasksUseCase
import com.example.domain.use_case.GetAllPeriodicTasksUseCase
import com.example.domain.use_case.GetAllTasksCountUseCase
import com.example.domain.use_case.GetAllTasksUseCase
import com.example.domain.use_case.GetGroupUseCase
import com.example.domain.use_case.GetGroupWithTasksUseCase
import com.example.domain.use_case.GetNoTimeTasksCountUseCase
import com.example.domain.use_case.GetNoTimeTasksUseCase
import com.example.domain.use_case.GetPlannedTasksUseCase
import com.example.domain.use_case.GetTaskUseCase
import com.example.domain.use_case.GetTasksForTodayCountUseCase
import com.example.domain.use_case.GetTasksForTodayUseCase
import com.example.domain.use_case.GetTasksPlannedCountUseCase
import com.example.domain.use_case.GetTasksWithFlagCountUseCase
import com.example.domain.use_case.GetTasksWithFlagUseCase
import com.example.domain.use_case.InsertGroupUseCase
import com.example.domain.use_case.SaveTaskUseCase
import io.mockk.mockk
import org.koin.dsl.module

val useCaseModule = module {
    single<TaskRepository> { mockk() }

    single { ClearAllTasksUseCase(get()) }
    single { DeleteGroupUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { EditGroupUseCase(get()) }
    single { EditTaskUseCase(get()) }
    single { GetAllGroupsUseCase(get()) }
    single { GetAllOneTimeTasksUseCase(get()) }
    single { GetAllPeriodicTasksUseCase(get()) }
    single { GetAllTasksCountUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetGroupUseCase(get()) }
    single { GetGroupWithTasksUseCase(get()) }
    single { GetNoTimeTasksCountUseCase(get()) }
    single { GetNoTimeTasksUseCase(get()) }
    single { GetPlannedTasksUseCase(get()) }
    single { GetTasksForTodayUseCase(get()) }
    single { GetTasksForTodayCountUseCase(get()) }
    single { GetTasksPlannedCountUseCase(get()) }
    single { GetTasksWithFlagCountUseCase(get()) }
    single { GetTasksWithFlagUseCase(get()) }
    single { GetTaskUseCase(get()) }
    single { InsertGroupUseCase(get()) }
    single { SaveTaskUseCase(get()) }
}
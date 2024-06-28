package com.example.data.di

import com.example.domain.repository.ITaskRepository
import com.example.domain.use_case.task.ClearAllTasksUseCase
import com.example.domain.use_case.group.DeleteGroupUseCase
import com.example.domain.use_case.task.DeleteTaskUseCase
import com.example.domain.use_case.group.EditGroupUseCase
import com.example.domain.use_case.task.EditTaskUseCase
import com.example.domain.use_case.group.GetAllGroupsUseCase
import com.example.domain.use_case.task.GetAllOneTimeTasksUseCase
import com.example.domain.use_case.task.GetAllPeriodicTasksUseCase
import com.example.domain.use_case.task.GetAllTasksCountUseCase
import com.example.domain.use_case.task.GetAllTasksUseCase
import com.example.domain.use_case.group.GetGroupUseCase
import com.example.domain.use_case.group.GetGroupWithTasksUseCase
import com.example.domain.use_case.task.GetNoTimeTasksCountUseCase
import com.example.domain.use_case.task.GetNoTimeTasksUseCase
import com.example.domain.use_case.task.GetPlannedTasksUseCase
import com.example.domain.use_case.task.GetTaskUseCase
import com.example.domain.use_case.task.GetTasksForTodayCountUseCase
import com.example.domain.use_case.task.GetTasksForTodayUseCase
import com.example.domain.use_case.task.GetTasksPlannedCountUseCase
import com.example.domain.use_case.task.GetTasksWithFlagCountUseCase
import com.example.domain.use_case.task.GetTasksWithFlagUseCase
import com.example.domain.use_case.group.InsertGroupUseCase
import com.example.domain.use_case.task.SaveTaskUseCase
import io.mockk.mockk
import org.koin.dsl.module

val useCaseModule = module {
    single<ITaskRepository> { mockk() }

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
package com.example.reminderapp.di

import com.example.domain.use_case.alarm.ChangeAlarmUseCase
import com.example.domain.use_case.alarm.ClearAlarmUseCase
import com.example.domain.use_case.alarm.CreateAlarmUseCase
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
import com.example.domain.use_case.reminder.CreateReminderUseCase
import com.example.domain.use_case.reminder.DeleteReminderGroupUseCase
import com.example.domain.use_case.reminder.DeleteReminderUseCase
import com.example.domain.use_case.reminder.EditReminderUseCase
import com.example.domain.use_case.task.SaveTaskUseCase
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
        GetAllGroupsUseCase(groupRepository = get())
    }

    factory<GetGroupUseCase> {
        GetGroupUseCase(groupRepository = get())
    }

    factory<GetGroupWithTasksUseCase> {
        GetGroupWithTasksUseCase(groupRepository = get())
    }

    factory<DeleteGroupUseCase> {
        DeleteGroupUseCase(groupRepository = get())
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
        InsertGroupUseCase(groupRepository = get())
    }

    factory<GetAllTasksCountUseCase> {
        GetAllTasksCountUseCase(taskRepository = get())
    }

    factory<EditGroupUseCase> {
        EditGroupUseCase(groupRepository = get())
    }

    factory<GetNoTimeTasksCountUseCase> {
        GetNoTimeTasksCountUseCase(taskRepository = get())
    }

    factory<GetNoTimeTasksUseCase> {
        GetNoTimeTasksUseCase(taskRepository = get())
    }

    factory<CreateReminderUseCase> {
        CreateReminderUseCase(saveTaskUseCase = get(), createAlarmUseCase = get())
    }

    factory<EditReminderUseCase> {
        EditReminderUseCase(editTaskUseCase = get(), changeAlarmUseCase = get())
    }

    factory<DeleteReminderUseCase> {
        DeleteReminderUseCase(deleteTaskUseCase = get(), getTaskUseCase = get(), clearAlarmUseCase = get())
    }

    factory<DeleteReminderGroupUseCase> {
        DeleteReminderGroupUseCase(getGroupWithTasksUseCase = get(), deleteReminderUseCase = get(), deleteGroupUseCase = get())
    }

    factory<CreateAlarmUseCase> {
        CreateAlarmUseCase(alarmManager = get())
    }

    factory<ClearAlarmUseCase> {
        ClearAlarmUseCase(alarmManager = get())
    }

    factory<ChangeAlarmUseCase> {
        ChangeAlarmUseCase(createAlarmUseCase = get(), clearAlarmUseCase = get())
    }
}
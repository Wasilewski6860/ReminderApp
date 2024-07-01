package com.example.reminderapp.di

import com.example.reminderapp.presentation.base.CalendarProvider
import com.example.reminderapp.presentation.base.ICalendarProvider
import com.example.reminderapp.presentation.create_reminder.CreateReminderViewModel
import com.example.reminderapp.presentation.editorlistsscreen.EditListsViewModel
import com.example.reminderapp.presentation.main.MainViewModel
import com.example.reminderapp.presentation.new_list.NewListViewModel
import com.example.reminderapp.presentation.reminder_list.ReminderListViewModel
import com.example.reminderapp.utils.TimeDateUtils
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationTestModule = module {
    viewModel<MainViewModel> {
        MainViewModel(
            getAllGroupsUseCase = get(),
            getTasksPlannedCountUseCase = get(),
            getTasksForTodayCountUseCase = get(),
            getTasksWithFlagCountUseCase = get(),
            getAllTasksCountUseCase = get(),
            getNoTimeTasksCountUseCase = get(),
            getTasksWithoutGroupCountUseCase = get()
        )
    }

    single<ICalendarProvider> { CalendarProvider() }

    single<TimeDateUtils> {
        TimeDateUtils(get())
    }

    viewModel<CreateReminderViewModel> {
        CreateReminderViewModel(
            createReminderUseCase = get(),
            getAllGroupsUseCase = get(),
            editReminderUseCase = get(),
            calendarProvider = get()
        )
    }

    viewModel<EditListsViewModel> {
        EditListsViewModel(
            getAllGroupsUseCase = get(),
            deleteReminderGroupUseCase = get()
        )
    }

    viewModel<ReminderListViewModel> {
        ReminderListViewModel(
            getGroupWithTasksUseCase = get(),
            getAllTasksUseCase = get(),
            getPlannedTasksUseCase = get(),
            getTasksForTodayUseCase = get(),
            getTasksWithFlagUseCase = get(),
            application = get(),
            editReminderUseCase = get(),
            deleteReminderUseCase = get(),
            getNoTimeTasksUseCase = get(),
            getTasksWithoutGroupUseCase = get()
        )
    }

    viewModel<NewListViewModel> {
        NewListViewModel(
            insertGroupUseCase = get(),
            editGroupUseCase = get()
        )
    }
}
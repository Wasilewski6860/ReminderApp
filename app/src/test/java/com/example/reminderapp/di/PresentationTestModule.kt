package com.example.reminderapp.di

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.reminderapp.presentation.base.CalendarProvider
import com.example.reminderapp.presentation.base.ICalendarProvider
import com.example.reminderapp.presentation.create_reminder.CreateReminderViewModel
import com.example.reminderapp.presentation.editorlistsscreen.ListsEditorViewModel
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.presentation.new_list.NewListViewModel
import com.example.reminderapp.presentation.reminder_list.ReminderListViewModel
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
            getNoTimeTasksCountUseCase = get()
        )
    }

    single<ICalendarProvider> { CalendarProvider() }

    viewModel<CreateReminderViewModel> {
        CreateReminderViewModel(
            createReminderUseCase = get(),
            getAllGroupsUseCase = get(),
            editReminderUseCase = get(),
            calendarProvider = get()
        )
    }

    viewModel<ListsEditorViewModel> {
        ListsEditorViewModel(
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
            getNoTimeTasksUseCase = get()
        )
    }

    viewModel<NewListViewModel> {
        NewListViewModel(
            insertGroupUseCase = get(),
            editGroupUseCase = get()
        )
    }
}
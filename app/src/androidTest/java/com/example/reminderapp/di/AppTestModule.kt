package com.example.reminderapp.di

import com.example.reminderapp.presentation.create_reminder.CreateReminderViewModel
import com.example.reminderapp.presentation.editorlistsscreen.ListsEditorViewModel
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.presentation.new_list.NewListViewModel
import com.example.reminderapp.presentation.reminder_list.ReminderListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appTestModule = module {
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

    viewModel<CreateReminderViewModel> {
        CreateReminderViewModel(
            createReminderUseCase = get(),
            getAllGroupsUseCase = get(),
            editReminderUseCase = get()
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
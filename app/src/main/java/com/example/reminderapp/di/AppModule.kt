package com.example.reminderapp.di

import android.app.AlarmManager
import android.content.Context
import com.example.reminderapp.notification.NotificationManager
import com.example.reminderapp.presentation.create_reminder.CreateReminderViewModel
import com.example.reminderapp.presentation.editorlistsscreen.ListsEditorViewModel
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.presentation.new_list.NewListViewModel
import com.example.reminderapp.presentation.reminder_list.ReminderListViewModel
import com.example.data.reminder.RemindAlarmManager
import com.example.reminderapp.receivers.AlarmBroadcastReceiver
import com.example.reminderapp.receivers.work.RemindWorkManager
import com.example.reminderapp.utils.TimeDateUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<RemindWorkManager> {
        RemindWorkManager(androidContext())
    }

    single<RemindAlarmManager> {
        RemindAlarmManager(context = androidContext(), receiverClass = AlarmBroadcastReceiver::class.java)
    }

    single<AlarmManager> {
        val context: Context = get()
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    single<NotificationManager> {
        NotificationManager(androidContext())
    }

    single<TimeDateUtils> {
        TimeDateUtils(get())
    }

    viewModel<MainViewModel> {
        MainViewModel(
            getAllGroupsUseCase = get(),
            getTasksPlannedCountUseCase = get(),
            getTasksForTodayCountUseCase = get(),
            getTasksWithFlagCountUseCase = get(),
            getAllTasksCountUseCase = get()
        )
    }

    viewModel<CreateReminderViewModel> {
        CreateReminderViewModel(
            createTaskUseCase = get(),
            getAllGroupsUseCase = get(),
            editTaskUseCase = get()
        )
    }

    viewModel<ListsEditorViewModel> {
        ListsEditorViewModel(
            getAllGroupsUseCase = get(),
            deleteGroupUseCase = get()
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
            editTaskUseCase = get(),
            deleteTaskUseCase = get()
        )
    }

    viewModel<NewListViewModel> {
        NewListViewModel(
            insertGroupUseCase = get(),
            editGroupUseCase = get()
        )
    }
}
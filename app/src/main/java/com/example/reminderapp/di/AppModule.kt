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
import com.example.domain.alarm.IRemindAlarmManager
import com.example.reminderapp.remind.receivers.AlarmBroadcastReceiver
import com.example.reminderapp.remind.work.RemindWorkManager
import com.example.reminderapp.utils.TimeDateUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<RemindWorkManager> {
        RemindWorkManager(androidContext())
    }

    single<IRemindAlarmManager> {
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
            deleteReminderUseCase = get()
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
package com.example.reminderapp.di

import android.app.AlarmManager
import android.content.Context
import com.example.reminderapp.notification.NotificationManager
import com.example.reminderapp.presentation.create_reminder.CreateReminderViewModel
import com.example.reminderapp.presentation.editorlistsscreen.EditListsViewModel
import com.example.reminderapp.presentation.main.MainViewModel
import com.example.reminderapp.presentation.new_list.NewListViewModel
import com.example.reminderapp.presentation.reminder_list.ReminderListViewModel
import com.example.data.reminder.RemindAlarmManager
import com.example.domain.alarm.IRemindAlarmManager
import com.example.reminderapp.presentation.base.CalendarProvider
import com.example.reminderapp.presentation.base.ICalendarProvider
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

    single<ICalendarProvider> { CalendarProvider() }

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
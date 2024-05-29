package com.example.reminderapp.di

import android.app.AlarmManager
import android.content.Context
import com.example.reminderapp.notification.NotificationManager
import com.example.reminderapp.presentation.creatorscreen.CreatorViewModel
import com.example.reminderapp.presentation.mainscreen.MainViewModel
import com.example.reminderapp.reminder.RemindAlarmManager
import com.example.reminderapp.reminder.work.RemindWorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<RemindWorkManager> {
        RemindWorkManager(androidContext())
    }

    single<RemindAlarmManager> {
        RemindAlarmManager(context = androidContext())
    }

    single<AlarmManager> {
        val context: Context = get()
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    single<NotificationManager> {
        NotificationManager(androidContext())
    }

    viewModel<MainViewModel> {
        MainViewModel(
            getAllTasksUseCase = get(),
            deleteTaskUseCase = get()
        )
    }

    viewModel<CreatorViewModel> {
        CreatorViewModel(
            saveTaskUseCase = get(),
            getTaskUseCase = get()
        )
    }

}
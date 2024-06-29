package com.example.reminderapp.presentation.base

import java.util.Calendar

interface ICalendarProvider {
    fun getInstance(): Calendar
}

class CalendarProvider : ICalendarProvider {
    override fun getInstance(): Calendar {
        return Calendar.getInstance()
    }
}


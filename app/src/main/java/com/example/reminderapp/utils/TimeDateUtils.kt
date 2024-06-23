package com.example.reminderapp.utils

import android.content.Context
import com.example.reminderapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeDateUtils(context: Context) {

    val timeDates = listOf<TimePeriodItem>(
        TimePeriodItem(
            context.getString(R.string.never_time_list_item),
            null
        ),
        TimePeriodItem(
            context.getString(R.string.five_min_time_list_item),
            300000
        ),
        TimePeriodItem(
            context.getString(R.string.every_15_min_time_list_item),
            900000
        ),
        TimePeriodItem(
            context.getString(R.string.every_30_min_time_list_item),
            1800000
        ),
        TimePeriodItem(
            context.getString(R.string.every_hour_time_list_item),
            3600000
        ),
        TimePeriodItem(
            context.getString(R.string.every_12_hours_time_list_item),
            43200000
        ),
        TimePeriodItem(
            context.getString(R.string.every_day_time_list_item),
            86400000
        ),
        TimePeriodItem(
            context.getString(R.string.every_week_time_list_item),
            604800016
        ),
        TimePeriodItem(
            context.getString(R.string.every_month_time_list_item),
            2629800000
        ),
        TimePeriodItem(
            context.getString(R.string.every_6_month_time_list_item),
            15778800000
        ),
        TimePeriodItem(
            context.getString(R.string.every_year_time_list_item),
            31557600000
        ),
    )

    private fun getPeriod(ms: Long?) : TimePeriodItem? = timeDates.firstOrNull { it.time == ms }

    fun getFormattedTime(ms: Long?): String {
        if (ms==null) return ""
        val date = Date(ms)
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    fun getFormattedPeriod(period: Long?): String? = getPeriod(period)?.name

}

data class TimePeriodItem(
    val name: String,
    val time: Long?
)


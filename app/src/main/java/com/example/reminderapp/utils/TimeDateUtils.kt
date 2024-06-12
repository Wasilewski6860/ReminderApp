package com.example.reminderapp.utils

class TimeDateUtils {
    companion object {
        val timeDates = listOf<TimePeriodItem>(
            TimePeriodItem(
                "Никогда",
                null
            ),
            TimePeriodItem(
                "Каждые 5 минут",
                300000
            ),
            TimePeriodItem(
                "Каждые 15 минут",
                900000
            ),
            TimePeriodItem(
                "Каждые полчаса",
                1800000
            ),
            TimePeriodItem(
                "Каждый час",
                3600000
            ),
            TimePeriodItem(
                "Каждые 12 часов",
                43200000
            ),
            TimePeriodItem(
                "Ежедневно",
                86400000
            ),
            TimePeriodItem(
                "Еженедельно",
                604800016
            ),
            TimePeriodItem(
                "Ежемесячно",
                2629800000
            ),
            TimePeriodItem(
                "Каждые полгода",
                15778800000
            ),
            TimePeriodItem(
                "Ежегодно",
                31557600000
            ),
        )
    }
}

data class TimePeriodItem(
    val name: String,
    val time: Long?
)
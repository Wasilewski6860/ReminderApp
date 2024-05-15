package com.example.data

import com.example.domain.model.Exception
import com.example.domain.model.TaskPeriodType

fun String.toPeriodicType(): TaskPeriodType {
    when(this) {
        TaskPeriodType.PERIODIC.toString() -> return TaskPeriodType.PERIODIC
        TaskPeriodType.ONE_TIME.toString() -> return TaskPeriodType.ONE_TIME
        else -> throw Exception.StringCastException("STRING CAST TO PERIODIC TYPE EXCEPTION: INVALID STRING")
    }
}
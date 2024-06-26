package com.example.data

import com.example.domain.model.Exception
import com.example.domain.model.TaskPeriodType

fun String.toPeriodicType(): TaskPeriodType {
    when(this.lowercase()) {
        TaskPeriodType.PERIODIC.toString().lowercase() -> return TaskPeriodType.PERIODIC
        TaskPeriodType.ONE_TIME.toString().lowercase() -> return TaskPeriodType.ONE_TIME
        TaskPeriodType.NO_TIME.toString().lowercase() -> return TaskPeriodType.NO_TIME
        else -> throw Exception.StringCastException("STRING CAST TO PERIODIC TYPE EXCEPTION: INVALID STRING")
    }
}
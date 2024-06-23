package com.example.domain.model

enum class TaskPeriodType(name: String) {
    PERIODIC("periodic"),
    ONE_TIME("one_time"),
    NO_TIME("no_time");

    override fun toString(): String {
        return name
    }

}

object Exception {
    class StringCastException(override val message: String): RuntimeException(message)
}

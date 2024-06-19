package com.example.reminderapp.presentation.base

interface  BaseDataSerializer<T> {
    fun serialize(obj: T): String
    fun deserialize(serialized: String): T
}
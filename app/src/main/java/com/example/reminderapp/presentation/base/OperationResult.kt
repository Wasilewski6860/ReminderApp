package com.example.reminderapp.presentation.base

sealed interface OperationResult<out T> {

    object NotStarted : OperationResult<Nothing>

    object Loading : OperationResult<Nothing>

    data class Success<T>(val data: T) : OperationResult<T>

    data class Error(val message: String) : OperationResult<Nothing>



}
package com.example.reminderapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService

fun EditText.setFocus(context: Context) {
    this.requestFocus()

    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Утилитарная функция для совершения действия с вью только если данные изменились.
 * Удобно использовать при рендеринге данных, которые могут измениться.
 *
 * @param data данные
 * @param action действие, которое нужно выполнить если данные изменились
 */
fun <T> View.actionIfChanged(data: T, action: (T) -> Unit) {
    val hashCode = data?.hashCode() ?: 0
    if (hashCode != tag) {
        tag = hashCode
        action(data)
    }
}

/**
 * Утилитарная функция для совершения действия с вью только если данные изменились.
 * Удобно использовать при рендеринге данных, которые могут измениться.
 * Данный метод содержит дополнительную переменную, в которой можно передать данные,
 * которые тоже должны учитываться при подсчете изменений, но которые не относятся напрямую
 * к основным данным
 *
 * @param data данные
 * @param addition дополнительные данные
 * @param action действие, которое нужно выполнить если данные изменились
 */
fun <T, V> View.actionIfChanged(data: T, addition: V, action: (T) -> Unit) {
    var result = data?.hashCode() ?: 0
    result = 31 * result + addition.hashCode()
    if (result != tag) {
        tag = result
        action(data)
    }
}
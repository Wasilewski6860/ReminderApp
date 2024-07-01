package com.example.reminderapp.presentation.create_reminder.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.domain.model.Group
import com.example.reminderapp.R
import com.example.reminderapp.utils.TimePeriodItem

class PeriodSpinnerAdapter(
    context: Context,
    private val onNoneSelected: () -> Unit,
    private val onItemSelected: (TimePeriodItem) -> Unit
) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listOf(context.getString(R.string.not_selected))) {


    private var periodItems = mutableListOf<TimePeriodItem>()

    fun submitList(periods: List<TimePeriodItem>) {
        periodItems.clear()
        periodItems.addAll(periods)
        val list = mutableListOf<String>()
        list.add(context.getString(R.string.not_selected))
        list.addAll(periodItems.map { it.name })
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        // +2 для "Не выбрано" и "Создать список"
        return periodItems.size + 1
    }

    override fun getItem(position: Int): String? {
        return when (position) {
            0 -> context.getString(R.string.not_selected)
            else -> periodItems[position - 1].name
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)
        val padding = context.resources.getDimensionPixelSize(R.dimen.tiny_margin)
        textView.setPadding(padding, padding, padding, padding)
        return view
    }

    fun handleItemSelected(position: Int) {
        when (position) {
            0 -> onNoneSelected()
            else -> onItemSelected(periodItems[position - 1])
        }
    }

    fun getTimePeriodPosition(time: Long?): Int {
        val index = periodItems.indexOfFirst { it.time == time }
        return if (index >= 0) index + 1 else 0 // +1 чтобы учесть "Не выбрано"
    }

    fun getTimePeriodName(time: Long): String? {
        return periodItems.firstOrNull{ it.time==time }?.name
    }
}

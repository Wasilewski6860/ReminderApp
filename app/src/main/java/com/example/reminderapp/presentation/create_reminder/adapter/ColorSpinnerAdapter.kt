package com.example.reminderapp.presentation.create_reminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.domain.model.Group
import com.example.reminderapp.R
import com.example.reminderapp.utils.ColorItem
import com.example.reminderapp.utils.TimePeriodItem

class ColorSpinnerAdapter(
    context: Context,
    private val onItemSelected: (ColorItem) -> Unit
) : ArrayAdapter<ColorItem>(context, R.layout.spinner_color_item_layout, listOf()) {


    private var colorItems = mutableListOf<ColorItem>()

    fun submitList(colors: List<ColorItem>) {
        colorItems.clear()
        colorItems.addAll(colors)
        notifyDataSetChanged()
    }
    override fun getCount(): Int {
        // +2 для "Не выбрано" и "Создать список"
        return colorItems.size
    }

    override fun getItem(position: Int): ColorItem {
        return colorItems[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_color_item_layout, parent, false)
        val textView = view.findViewById<TextView>(R.id.colorTv)
        val colorView = view.findViewById<View>(R.id.colorView)
        val spinnerItem = colorItems[position]
        textView.text = spinnerItem.name
        spinnerItem.color?.let { colorView.setBackgroundColor(it) }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    fun handleItemSelected(position: Int) {
        onItemSelected(colorItems[position])
    }

    fun getColorPosition(color: Int?): Int {
        val index = colorItems.indexOfFirst { it.color == color }
        return if (index >= 0) index  else 0 // +1 чтобы учесть "Не выбрано"
    }
//
//    fun getColor(time: Long): String? {
//        return periods.firstOrNull{ it.time==time }?.name
//    }
}

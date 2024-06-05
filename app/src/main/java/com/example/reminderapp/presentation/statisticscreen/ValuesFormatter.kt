package com.example.reminderapp.presentation.statisticscreen

import android.icu.text.DecimalFormat
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class ValuesFormatter : ValueFormatter() {

    private val format = DecimalFormat("%b")

    override fun getBarLabel(barEntry: BarEntry?): String {
        return format.format(barEntry?.y)
    }

}
package com.example.reminderapp.presentation.creatorscreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.reminderapp.R
import com.example.reminderapp.animations.playFloatingButtonAnimation
import com.example.reminderapp.reminder.work.RemindWorkManager
import com.example.reminderapp.databinding.ReminderCreatorFragmentBinding
import com.example.reminderapp.presentation.BackActionInterface
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale
import com.example.reminderapp.reminder.RemindAlarmManager
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import kotlin.random.Random

class TaskCreatorFragment : Fragment(), KoinComponent, BackActionInterface {

    private lateinit var binding: ReminderCreatorFragmentBinding
    private val viewModel by viewModel<CreatorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReminderCreatorFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun goBack(navController: NavController) {

    }

    private fun getKeyByValue(map: Map<Long, String>, value: String): Long? {
        return map.entries.firstOrNull { it.value == value }?.key
    }

    // Temp date and time picker variant for one time reminder
    private fun showDateAndTimePickers() {
        val formatDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val getDate = Calendar.getInstance()

        // Date Picker
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, monthOfYear)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Time Picker
                val timePicker = TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        selectedDate.set(Calendar.MINUTE, minute)

                        // Format and display the selected date-time
                    },
                    getDate.get(Calendar.HOUR_OF_DAY),
                    getDate.get(Calendar.MINUTE),
                    true // set true for 24-hour format
                )
                timePicker.show()
            },
            getDate.get(Calendar.YEAR),
            getDate.get(Calendar.MONTH),
            getDate.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

}
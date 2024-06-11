package com.example.reminderapp.presentation.creatorscreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.reminderapp.databinding.FragmentAddReminderBinding
import com.example.reminderapp.presentation.BackActionInterface
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale
import org.koin.core.component.KoinComponent

class TaskCreatorFragment : Fragment(), KoinComponent, BackActionInterface {

    private lateinit var binding: FragmentAddReminderBinding
    private val viewModel by viewModel<CreatorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddReminderBinding.inflate(inflater, container, false)

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
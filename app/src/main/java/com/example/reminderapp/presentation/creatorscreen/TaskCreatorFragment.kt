package com.example.reminderapp.presentation.creatorscreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
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
import com.example.reminderapp.databinding.ReminderCreatorFragmentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class TaskCreatorFragment : Fragment() {

    companion object {
        const val BOTH = "both"
        const val DATE_AND_TIME = "date and time"
        const val REMINDER_NAME = "reminder name"
    }

    private lateinit var binding: ReminderCreatorFragmentBinding
    private val viewModel by viewModel<CreatorViewModel>()
    private val spinnerTimeTextList = SpinnerPeriodicTimeText.getTimesList()
    private var incompletenessOfTheEnteredData: String = BOTH
    private var isTimeHasChosen = false
    private var isReminderPeriodic = false
    private var spinnerSelectedColor = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReminderCreatorFragmentBinding.inflate(inflater, container, false)

        binding.apply {

            val colorSpinnerAdapter = createColorSpinnerAdapter(SpinnerColors.spinnerColorsList)
            reminderColorSpinner.adapter = colorSpinnerAdapter

            val timeSpinnerAdapter = createTimeSpinnerAdapter(spinnerTimeTextList)
            reminderTimePickerSpinner.adapter = timeSpinnerAdapter

        }

        initListeners(findNavController())

        return binding.root
    }

    private fun initListeners(navController: NavController) = with(binding) {
        requireActivity().findViewById<FloatingActionButton>(R.id.floatingButton)
            .setOnClickListener {
                if (checkForCompletenessOfDataEntry()) {
                    viewModel.saveTaskInDatabase(collectInformation())
                    navController.navigate(
                        resId = R.id.mainFragment,
                        args = null,
                        navOptions = NavOptions.Builder().setExitAnim(R.anim.slide_out_anim).build()
                    )
                } else {
                    when (incompletenessOfTheEnteredData) {
                        BOTH -> Toast.makeText(
                            requireActivity().applicationContext,
                            "Enter reminder name and choose time HARDCODE",
                            Toast.LENGTH_SHORT
                        ).show()

                        REMINDER_NAME -> Toast.makeText(
                            requireActivity().applicationContext,
                            "Enter reminder name HARDCODE",
                            Toast.LENGTH_SHORT
                        ).show()

                        DATE_AND_TIME -> Toast.makeText(
                            requireActivity().applicationContext,
                            "Choose time HARDCODE",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        reminderTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            reminderSpinnerOrButtonPlaceHolder.visibility = View.VISIBLE
            val checkedButton = binding.root.findViewById<RadioButton>(checkedId)
            when (checkedButton) {
                periodicReminderRadioButton -> {
                    isTimeHasChosen = true
                    isReminderPeriodic = true
                    reminderDateAndTimeTextView.visibility = View.GONE
                    reminderTimeSpinnerHolder.visibility = View.VISIBLE
                }

                onetimeReminderRadioButton -> {
                    isTimeHasChosen = false
                    isReminderPeriodic = false
                    reminderTimeSpinnerHolder.visibility = View.GONE
                    showDateAndTimePickers()
                }
            }
        }

        reminderColorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerSelectedColor = parent?.getItemAtPosition(position) as Int
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun collectInformation(): Task {
        return Task(
            id = 0,
            name = binding.reminderNameEditTextView.text.toString(),
            description = if (!binding.reminderDescriptionEditTextView.text.isNullOrBlank())
                binding.reminderDescriptionEditTextView.text.toString()
            else "",
            reminderCreationTime = 0, // temp code string
            reminderTimeTarget = 0, // temp too
            type = if (isReminderPeriodic) TaskPeriodType.PERIODIC else TaskPeriodType.ONE_TIME,
            color = spinnerSelectedColor
            )
    }

    private fun checkForCompletenessOfDataEntry(): Boolean {
        incompletenessOfTheEnteredData =
            if (binding.reminderNameEditTextView.text.isNullOrBlank() && !isTimeHasChosen) {
                BOTH
            } else if (binding.reminderNameEditTextView.text.isNullOrBlank()) {
                REMINDER_NAME
            } else {
                DATE_AND_TIME
            }
        return !(binding.reminderNameEditTextView.text.isNullOrBlank() || !isTimeHasChosen)
    }

    private fun createTimeSpinnerAdapter(
        timesList: List<String>
    ): ArrayAdapter<String> {
        return object : ArrayAdapter<String>(
            requireActivity().applicationContext,
            R.layout.spinner_time_item,
            timesList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater
                    .from(requireContext())
                    .inflate(R.layout.spinner_time_item, parent, false)
                val timeItem = getItem(position)

                val timeView = view.findViewById<TextView>(R.id.spinnerTimeItemView)
                timeView.text =
                    timeItem ?: requireActivity().applicationContext.getString(R.string.stub)

                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return getView(position, convertView, parent)
            }
        }
    }

    private fun createColorSpinnerAdapter(
        colorsList: List<Int>
    ): ArrayAdapter<Int> {
        return object : ArrayAdapter<Int>(
            requireActivity().applicationContext,
            R.layout.spinner_color_item,
            colorsList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater
                    .from(requireContext())
                    .inflate(R.layout.spinner_color_item, parent, false)
                val colorItem = getItem(position)

                val colorView = view.findViewById<View>(R.id.spinnerItemColorView)
                colorView.setBackgroundColor(colorItem ?: 0)

                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return getView(position, convertView, parent)
            }
        }
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
                        binding.reminderDateAndTimeTextView.text =
                            formatDate.format(selectedDate.time)
                        binding.reminderDateAndTimeTextView.visibility = View.VISIBLE
                        isTimeHasChosen = true
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
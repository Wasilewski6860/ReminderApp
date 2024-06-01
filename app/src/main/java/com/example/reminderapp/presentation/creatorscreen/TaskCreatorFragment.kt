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
import com.example.reminderapp.reminder.work.RemindWorkManager
import com.example.reminderapp.databinding.ReminderCreatorFragmentBinding
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale
import com.example.reminderapp.reminder.RemindAlarmManager
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import kotlin.random.Random

class TaskCreatorFragment : Fragment(), KoinComponent {

    companion object {
        const val BOTH = "both"
        const val DATE_AND_TIME = "date and time"
        const val REMINDER_NAME = "reminder name"
    }

    private lateinit var binding: ReminderCreatorFragmentBinding
    private val viewModel by viewModel<CreatorViewModel>()
    private lateinit var spinnerTimeTextList: List<String> // in future need delete lateinit from this
    private lateinit var timesDict: Map<Long, String>
    private var selectedTime: String = ""
    private var incompletenessOfTheEnteredData: String = BOTH
    private var isTimeHasChosen = false
    private var isReminderPeriodic = false
    private var spinnerSelectedColor = 0
    private var timeDifference: Long = 0

    /**
     * Использовать при создании, изменении и удалении напоминаний
     */
    private val remindWorkManager: RemindWorkManager by inject()
    private val remindAlarmManager: RemindAlarmManager by inject()
    val name = Random.nextInt(20).toString()
    val description = Random.nextInt(20).toString()

    /**
    Айдишник таска, передаваемый как аргумент в action навигации
    Например:
    val action =
    MainFragmentDirections.actionMainFragmentToCreateTaskFragment(
    id = task.taskId, isSaved = false
    )
    findNavController().navigate(action)

    Если taskId!=-1, значит нужно взять таск с бд и заполнить соотв.поля. фрагмента
     */
    private  var taskId: Int = -1
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            taskId = it.getInt("taskId")
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReminderCreatorFragmentBinding.inflate(inflater, container, false)

        spinnerTimeTextList = SpinnerPeriodicTime.getTimesList(requireActivity().applicationContext)
        timesDict = SpinnerPeriodicTime.getTimesDict(requireActivity().applicationContext)
        val handler = Handler(Looper.getMainLooper())

        binding.apply {

            val colorSpinnerAdapter = createColorSpinnerAdapter(SpinnerColors.spinnerColorsList)
            reminderColorSpinner.adapter = colorSpinnerAdapter

            val timeSpinnerAdapter = createTimeSpinnerAdapter(spinnerTimeTextList)
            reminderTimePickerSpinner.adapter = timeSpinnerAdapter

        }

        receivingInformation()
        initListeners(findNavController(), handler)

        return binding.root
    }

    private fun initListeners(navController: NavController, handler: Handler) = with(binding) {
        requireActivity().findViewById<FloatingActionButton>(R.id.floatingButton)
            .setOnClickListener {
                if (checkForCompletenessOfDataEntry()) {
                    when (arguments) {
                        null -> viewModel.saveTaskInDatabase(collectInformation())
                        else -> viewModel.editTaskInDatabase(collectInformation())
                    }
                    goBack(navController)
                } else {
                    val defaultHintColor = reminderNameEditTextView.currentHintTextColor
                    val defaultRadioGroupTextColor = periodicReminderRadioButton.currentTextColor
                    highlightFields(
                        incompletenessOfTheEnteredData,
                        defaultHintColor,
                        defaultRadioGroupTextColor,
                        handler
                    )
                    when (incompletenessOfTheEnteredData) {
                        BOTH -> {
                            Toast.makeText(
                                requireActivity().applicationContext,
                                "Enter reminder name and choose time HARDCODE",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        REMINDER_NAME -> {
                            Toast.makeText(
                                requireActivity().applicationContext,
                                "Enter reminder name HARDCODE",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        DATE_AND_TIME -> {
                            Toast.makeText(
                                requireActivity().applicationContext,
                                "Choose time HARDCODE",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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

        reminderTimePickerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedTime = parent?.getItemAtPosition(position) as String
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        backToMainScreenButton.setOnClickListener { goBack(navController) }

        deleteTaskButton.setOnClickListener {
            when (arguments) {
                null -> { goBack(navController) }
                else -> {
                    arguments?.getString(MainFragment.TASK_DATA)?.let {
                        Gson().fromJson(it, Task::class.java)
                    }?.let { task -> viewModel.deleteTask(task) }
                    goBack(navController)
                }
            }
        }

    }

    private fun goBack(navController: NavController) {
        navController.navigate(
            resId = R.id.mainFragment,
            args = null,
            navOptions = NavOptions.Builder().setExitAnim(R.anim.slide_out_anim).build()
        )
    }

    @SuppressLint("ResourceType")
    private fun receivingInformation() = with(binding) {
        arguments?.getString(MainFragment.TASK_DATA)?.let { data ->
            val receivedData = Gson().fromJson(data, Task::class.java)
            reminderNameEditTextView.setText(receivedData.name)
            reminderDescriptionEditTextView.setText(receivedData.description)
            // set color
            if (receivedData.type == TaskPeriodType.ONE_TIME) {
                reminderTypeRadioGroup.check(0) // do something with that
            } else {

            }
        }
    }

    private fun highlightFields(
        type: String,
        defaultHintColor: Int,
        defaultRadioGroupTextColor: Int,
        handler: Handler
    ) = with(binding) {
        when (type) {
            BOTH -> {
                reminderNameEditTextView.setHintTextColor(android.graphics.Color.RED)
                periodicReminderRadioButton.setTextColor(android.graphics.Color.RED)
                onetimeReminderRadioButton.setTextColor(android.graphics.Color.RED)
                handler.postDelayed({
                    reminderNameEditTextView.apply {
                        clearFocus()
                        setHintTextColor(defaultHintColor)
                    }
                    periodicReminderRadioButton.apply {
                        clearFocus()
                        setTextColor(defaultRadioGroupTextColor)
                    }
                    onetimeReminderRadioButton.apply {
                        clearFocus()
                        setTextColor(defaultRadioGroupTextColor)
                    }
                }, 1000)
            }

            REMINDER_NAME -> {
                reminderNameEditTextView.setHintTextColor(android.graphics.Color.RED)
                handler.postDelayed({
                    reminderNameEditTextView.apply {
                        clearFocus()
                        setHintTextColor(defaultHintColor)
                    }
                }, 1000)
            }

            DATE_AND_TIME -> {
                periodicReminderRadioButton.setTextColor(android.graphics.Color.RED)
                onetimeReminderRadioButton.setTextColor(android.graphics.Color.RED)
                handler.postDelayed({
                    periodicReminderRadioButton.apply {
                        clearFocus()
                        setTextColor(defaultRadioGroupTextColor)
                    }
                    onetimeReminderRadioButton.apply {
                        clearFocus()
                        setTextColor(defaultRadioGroupTextColor)
                    }
                }, 1000)
            }

            else -> {}
        }
    }

    private fun collectInformation(): Task {
        val id = if (arguments == null)
            0
        else
            arguments?.getString(MainFragment.TASK_DATA)
                ?.let { Gson().fromJson(it, Task::class.java).id }

        return Task(
            id = id!!,
            name = binding.reminderNameEditTextView.text.toString(),
            description = if (!binding.reminderDescriptionEditTextView.text.isNullOrBlank())
                binding.reminderDescriptionEditTextView.text.toString()
            else "",
            reminderCreationTime = System.currentTimeMillis(),
            reminderTimeTarget = (
                    if (!isReminderPeriodic)
                        timeDifference
                    else
                        getKeyByValue(timesDict, selectedTime)
                    )!!,
            type = if (isReminderPeriodic) TaskPeriodType.PERIODIC else TaskPeriodType.ONE_TIME,
            color = spinnerSelectedColor
        )
    }

    private fun getKeyByValue(map: Map<Long, String>, value: String): Long? {
        return map.entries.firstOrNull { it.value == value }?.key
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
                        timeDifference = viewModel.getTimeDifferenceInMilliseconds(
                            formatDate.format(getDate.time),
                            formatDate.format(selectedDate.time)
                        )
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
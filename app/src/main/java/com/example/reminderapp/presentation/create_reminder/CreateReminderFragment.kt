package com.example.reminderapp.presentation.create_reminder

import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.databinding.FragmentCreateReminderBinding
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.reminder.RemindAlarmManager
import com.example.reminderapp.utils.ColorItem
import com.example.reminderapp.utils.ColorsUtils
import com.example.reminderapp.utils.TimeDateUtils
import com.example.reminderapp.utils.setFocus
import com.example.reminderapp.utils.showSnackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class CreateReminderFragment : Fragment(), MenuProvider {

    companion object {
        fun newInstance() = CreateReminderFragment()
    }

    private var _binding: FragmentCreateReminderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateReminderViewModel by viewModel()
    private val remindAlarmManager: RemindAlarmManager by inject()

    var selectedGroup: Group? = null
    var selectedPeriod: Long? = null
    var selectedTime: Long? = null
    var selectedColor: Int? = null
    lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
        selectedGroup = arguments?.getSerializable("selected_group") as Group?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateReminderBinding.inflate(layoutInflater, container, false)
        val activity = (activity as MainActivity)
        activity.getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.fetchGroups()
        setupObservers()

        setSpinnerColor()
        setSpinnerPeriod()

        setupSwitches()

        binding.selectedDateTv.setOnClickListener {
            showDateAndTimePickers()
        }



    }

    fun setupSwitches() {
        binding.remindSwitch.setOnCheckedChangeListener { _, isChecked ->
            val constraintLayout = binding.dateContainer
            val linearLayout = binding.dateAndPeriodLl
            if (isChecked) {
//                AnimationUtils.expandLayoutToBottom(
//                    baseContainer = constraintLayout,
//                    neededToExpandContainer = linearLayout
//                )
                // устанавливаем высоту LinearLayout в 0dp в начале
                val layoutParams = linearLayout.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = 0
                linearLayout.layoutParams = layoutParams

                // создаем анимацию изменения высоты LinearLayout
                val animator = ValueAnimator.ofInt(0, getContentHeight(binding.dateAndPeriodLl)).apply {
                    duration = 500 // длительность анимации в миллисекундах
                    addUpdateListener { animation ->
                        val value = animation.animatedValue as Int
                        layoutParams.height = value
                        linearLayout.layoutParams = layoutParams
                        // вычисляем позицию Y для нижней части LinearLayout
                        val y: Float = constraintLayout.height.toFloat() - linearLayout.height.toFloat()
                        // устанавливаем позицию Y для нижней части LinearLayout
                        linearLayout.y = y
                    }
                }
                // запускаем анимацию
                animator.start()

                selectedTime = null
                selectedPeriod = null
                binding.selectedPeriodSpinner.setSelection(0)
                binding.selectedDateTv.text = "не установлено"
            }
            else {

//                AnimationUtils.collapseLayoutToTop(
//                    baseContainer = constraintLayout,
//                    neededToCollapseContainer = linearLayout
//                )
//
                // устанавливаем высоту LinearLayout в заданную величину в начале
                val layoutParams = linearLayout.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = getContentHeight(binding.dateAndPeriodLl)
                linearLayout.layoutParams = layoutParams

                // создаем анимацию изменения высоты LinearLayout
                val animator = ValueAnimator.ofInt(getContentHeight(binding.dateAndPeriodLl), 0).apply {
                    duration = 500 // длительность анимации в миллисекундах
                    addUpdateListener { animation ->
                        val value = animation.animatedValue as Int
                        layoutParams.height = value
                        linearLayout.layoutParams = layoutParams

                        // вычисляем позицию Y для нижней части LinearLayout
                        val y: Float = constraintLayout.height.toFloat() - linearLayout.height.toFloat()
                        // устанавливаем позицию Y для нижней части LinearLayout
                        linearLayout.y = y
                    }
                }

                animator.start()
            }
        }

        binding.flagSwitch.setOnCheckedChangeListener {  _, isChecked ->
            binding.flagIv.visibility = if(isChecked) View.VISIBLE else View.GONE
        }
    }
    
    fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.contentLayout.visibility = View.VISIBLE
                            binding.loadingLayout.visibility = View.GONE
                            setSpinnerGroup(it.data)
                        }
                        is UiState.Loading -> {
                            binding.contentLayout.visibility = View.GONE
                            binding.loadingLayout.visibility = View.VISIBLE
                        }
                        is UiState.Error -> {
                            binding.contentLayout.visibility = View.VISIBLE
                            binding.loadingLayout.visibility = View.GONE
                            showSnackbar(it.message, requireActivity().findViewById(R.id.rootView))
                        }
                    }
                }

            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveResult.collect {
                    if (it != (-1).toLong()) {
                        task.id = it.toInt()
                        remindAlarmManager.createAlarm(task)
                        val fragment = MainFragment()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragmentContainerView, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                }

            }
        }


    }

    fun setSpinnerPeriod() {
        val repeatTimeSpinnerItems = TimeDateUtils(requireContext()).timeDates.map { timeDate -> timeDate.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, repeatTimeSpinnerItems)
        val spinner = binding.selectedPeriodSpinner
        spinner.adapter = adapter

        binding.selectedPeriodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedPeriod = TimeDateUtils(requireContext()).timeDates[pos].time
            }
        }
    }

    fun setSpinnerGroup(groups: List<Group>) {
        val groupSpinnerItems = groups.map { group -> group.groupName }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groupSpinnerItems)
        val spinner = binding.selectedListSpinner
        spinner.adapter = adapter

        if (selectedGroup!=null) spinner.setSelection(groups.indexOf(selectedGroup))
        binding.selectedListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedGroup = groups[pos]
            }
        }
    }

    fun setSpinnerColor() {
        val groupSpinnerItems = ColorsUtils(requireContext()).colors

        val adapter = object: ArrayAdapter<ColorItem>(requireContext(), R.layout.spinner_color_item_layout, groupSpinnerItems) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_color_item_layout, parent, false)
                val textView = view.findViewById<TextView>(R.id.colorTv)
                val colorView = view.findViewById<View>(R.id.colorView)
                val spinnerItem = groupSpinnerItems[position]
                textView.text = spinnerItem.name
                spinnerItem.color?.let { colorView.setBackgroundColor(it) }
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                return getView(position, convertView, parent)
            }
        }
        val spinner = binding.selectedColorSpinner
        spinner.adapter = adapter

        binding.selectedListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedColor = groupSpinnerItems[pos].color
            }
        }
    }
    //TODO переделать
    private fun saveTask() = with(binding){
        if (isInputValid()) {
            val name = reminderNameEt.text.toString()
            val description = reminderNameEt.text.toString()
            val isFlag = flagSwitch.isChecked

            task = Task(
                name = name,
                description = description,
                reminderCreationTime = Calendar.getInstance().timeInMillis,
                reminderTime = selectedTime!!,
                reminderTimePeriod = selectedPeriod!!,
                type = if (selectedPeriod != null) TaskPeriodType.ONE_TIME else TaskPeriodType.PERIODIC,
                isActive = true,
                isMarkedWithFlag = flagSwitch.isChecked,
                groupId = selectedGroup!!.groupId,
                color = selectedColor!!
            )
            viewModel.saveTask(
                task
            )
        }
    }

    private fun showDateAndTimePickers() {
        val dateFormat = SimpleDateFormat("E, dd.MM.yyyy 'г.' HH:mm", Locale.getDefault())
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
                        val dateString = dateFormat.format(selectedDate.time)
                        selectedTime = selectedDate.time.time
                        binding.selectedDateTv.text = dateString
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

    fun isInputValid(): Boolean {
        with(binding) {
            val name = reminderNameEt.text.toString()

            if (name.isNullOrEmpty()){
                reminderNameEt.setFocus(requireContext())
                reminderNameEt.setError("Имя не может быть пустым")
                return false
            }
            else if(selectedTime == null) {
                showSnackbar("Дата напоминания должна быть выбрана", requireActivity().findViewById(R.id.rootView))
                return false
            }
            else if(selectedGroup == null) {
                showSnackbar("Список должен быть выбран", requireActivity().findViewById(R.id.rootView))
                return false
            }
            else if(selectedColor == null) {
                showSnackbar("Цвет должен быть выбран", requireActivity().findViewById(R.id.rootView))
                return false
            }

        }
        return true
    }

    fun getContentHeight(linearLayout: LinearLayout): Int {
        var height = 0
        for (i in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(i)
            child.measure(0, 0)
            height += child.measuredHeight
        }
        return height
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.create_task_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            android.R.id.home -> {
                requireActivity().onBackPressed()
                requireActivity().supportFragmentManager.popBackStack()
                return true
            }
            R.id.action_save -> {
//                //TODO добавить сохранение
//                val fragment = MainFragment()
//                val transaction = requireActivity().supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.fragmentContainerView, fragment)
//                transaction.addToBackStack(null)
//                transaction.commit()
                saveTask()
                return true
            }
        }
        return true
    }
}
package com.example.reminderapp.presentation.create_reminder

import android.Manifest
import android.animation.ValueAnimator
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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
import com.example.reminderapp.presentation.interfaces.BackActionInterface
import com.example.reminderapp.presentation.interfaces.DataReceiving
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants
import com.example.reminderapp.utils.ColorItem
import com.example.reminderapp.utils.ColorsUtils
import com.example.reminderapp.utils.TimeDateUtils
import com.example.reminderapp.utils.setFocus
import com.example.reminderapp.utils.setPaddingToInset
import com.example.reminderapp.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import java.util.Locale


class CreateReminderFragment : Fragment(), MenuProvider, BackActionInterface, DataReceiving {

    companion object {
        fun newInstance() = CreateReminderFragment()
    }

    private var _binding: FragmentCreateReminderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateReminderViewModel by viewModel()

    val dateFormat = SimpleDateFormat("E, dd.MM.yyyy 'г.' HH:mm", Locale.getDefault())

    private var taskId: Int? = null
    var selectedGroup: Int? = null
    var selectedPeriod: Long? = null
    var selectedTime: Long? = null
    var selectedColor: Int? = null
    var taskType: TaskPeriodType? = null

    var hasNotificationPermisions = false
    var hasScheduleExactAlarmPermisions = false


    private lateinit var callback: OnBackPressedCallback


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReminderBinding.inflate(layoutInflater, container, false)

        checkPermissions()

        edgeToEdge()
        setupToolbar()

        setupOnBackPressed()

        viewModel.fetchGroups()
        setupObservers()

        receiveData()

        binding.selectedDateTv.setOnClickListener {
            showDateAndTimePickers()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }


    override fun receiveData() {
        setupSwitches()
        arguments?.let {
            val taskSerialized: Task? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(
                    FragmentNavigationConstants.TASK_KEY,
                    Task::class.java
                )
            } else {
                it.getSerializable(
                    FragmentNavigationConstants.TASK_KEY
                ) as Task?
            }
            if (taskSerialized != null) {
                taskId = taskSerialized.id
                fillViews(taskSerialized)
            }
        }
        setSpinnerColor()
        setSpinnerPeriod()
    }

    private fun fillViews(task: Task) = with(binding) {
        reminderNameEt.setText(task.name)
        reminderDescriptionEt.setText(task.description)

        val dateString = dateFormat.format(task.reminderTime)

        remindSwitch.isChecked = true
        selectedTime = task.reminderTime
        selectedDateTv.text = dateString
        selectedPeriod = task.reminderTimePeriod

        binding.flagSwitch.isChecked = task.isMarkedWithFlag
        selectedColor = task.color
        selectedGroup = task.groupId

    }
    private fun setupSwitches() {
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

    private fun setupObservers() {
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
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setSpinnerPeriod() {
        val timeDates = TimeDateUtils(requireContext()).timeDates
        val repeatTimeSpinnerItems = timeDates.map { timeDate -> timeDate.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, repeatTimeSpinnerItems)
        val spinner = binding.selectedPeriodSpinner
        spinner.adapter = adapter

        if (selectedPeriod!=null) spinner.setSelection(timeDates.indexOfFirst{it.time == selectedPeriod})
        binding.selectedPeriodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedPeriod = TimeDateUtils(requireContext()).timeDates[pos].time
                taskType = if (TimeDateUtils(requireContext()).timeDates[pos].time != null) {
                    TaskPeriodType.PERIODIC
                } else {
                    TaskPeriodType.ONE_TIME
                }
            }
        }
    }

    private fun setSpinnerGroup(groups: List<Group>) {
        val groupSpinnerItems = groups.map { group -> group.groupName }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groupSpinnerItems)
        val spinner = binding.selectedListSpinner
        spinner.adapter = adapter

        if (selectedGroup!=null){
            spinner.setSelection(groups.indexOfFirst{it.groupId == selectedGroup})
        }
        else if (groups.isNotEmpty()) spinner.setSelection(0)
        binding.selectedListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedGroup = groups[pos].groupId
            }
        }
    }

    private fun setSpinnerColor() {
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

        if (selectedColor!=null) spinner.setSelection(groupSpinnerItems.indexOfFirst{it.color == selectedColor})

        binding.selectedColorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedColor = groupSpinnerItems[pos].color
            }
        }
    }
    //TODO переделать
    private fun saveTask() = with(binding) {

        if (isInputValid() && hasNotificationPermisions && hasScheduleExactAlarmPermisions) {
            val name = reminderNameEt.text.toString()
            val description = reminderDescriptionEt.text.toString()

            viewModel.saveTask(
                taskId = taskId,
                taskName = name,
                taskDesc = description,
                taskCreationTime = Calendar.getInstance().timeInMillis,
                taskTime = selectedTime?:Calendar.getInstance().timeInMillis,
                taskTimePeriod = selectedPeriod ?: 0,
                taskType = taskType!!,
                isActive = true,
                isMarkedWithFlag = flagSwitch.isChecked,
                groupId = selectedGroup!!,
                taskColor = selectedColor!!
            )
            navigateBack()
        }
        else if(!(hasNotificationPermisions && hasScheduleExactAlarmPermisions)) checkPermissions()
    }

    private fun showDateAndTimePickers() {
        val getDate = Calendar.getInstance()

        // Date Picker
        val datePicker = DatePickerDialog(
            requireContext(), R.style.PickerDialogStyle,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, monthOfYear)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Time Picker
                val timePicker = TimePickerDialog(
                    requireContext(), R.style.PickerDialogStyle,
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
                timePicker.getButton(Dialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                timePicker.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            },
            getDate.get(Calendar.YEAR),
            getDate.get(Calendar.MONTH),
            getDate.get(Calendar.DAY_OF_MONTH)

        )

        datePicker.show()
        datePicker.getButton(Dialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        datePicker.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    private fun isInputValid(): Boolean {
        with(binding) {
            val name = reminderNameEt.text.toString()

            if (name.isNullOrEmpty()){
                reminderNameEt.setFocus(requireContext())
                reminderNameEt.setError("Имя не может быть пустым")
                return false
            }
            else if(selectedTime == null && selectedPeriod == null) {
                showSnackbar("Дата напоминания и/или периодичность должны быть выбраны", requireActivity().findViewById(R.id.rootView))
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

    private fun getContentHeight(linearLayout: LinearLayout): Int {
        var height = 0
        for (i in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(i)
            child.measure(0, 0)
            height += child.measuredHeight
        }
        return height
    }

    fun setupOnBackPressed() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    private fun setupToolbar() = with(binding) {
        val activity = (activity as MainActivity)
        activity.setSupportActionBar(createToolbar)

        activity.supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun edgeToEdge() = with(binding) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
            createToolbar setPaddingToInset statusBars()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.create_task_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            android.R.id.home -> {
                navigateBack()
                return true
            }
            R.id.action_save -> {
                saveTask()
                return true
            }
        }
        return true
    }

    override fun navigateBack() {
        parentFragmentManager.popBackStack()
    }


    fun checkPermissions() {
        checkNotificationPermission()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkScheduleExactPermission()
        }
        else hasScheduleExactAlarmPermisions = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }
        else hasNotificationPermisions = true
    }


    private fun checkNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                hasNotificationPermisions = true
            }
            shouldShowRequestPermissionRationale(permission) -> {
                Snackbar.make(
                    requireActivity().findViewById(R.id.rootView),
                    getString(R.string.notification_permission_required),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.go_to_settings)) {
                    val uri: Uri = Uri.fromParts("package", requireContext().getPackageName(), null)
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        data = uri
                        startActivity(this)
                    }
                }.show()
            }
            else -> {
                requestNotificationPermissionLauncher.launch(permission)
            }
        }
    }

    private fun checkScheduleExactPermission() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager!!.canScheduleExactAlarms()) {
            // If not, request the SCHEDULE_EXACT_ALARM permission
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.setData(Uri.fromParts("package", requireContext().getPackageName(), null))
            startActivity(intent)
        }
        else hasScheduleExactAlarmPermisions = true
    }

    val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            hasNotificationPermisions = isGranted
        }

    val requestScheduleExactPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            hasScheduleExactAlarmPermisions = isGranted
        }
}
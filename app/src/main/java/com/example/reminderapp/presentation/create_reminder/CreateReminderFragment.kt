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
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
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
import com.example.reminderapp.utils.ColorsUtils
import com.example.reminderapp.utils.TimeDateUtils
import com.example.reminderapp.utils.setFocus
import com.example.reminderapp.utils.setPaddingToInset
import com.example.reminderapp.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import com.example.reminderapp.presentation.base.OperationResult
import com.example.reminderapp.presentation.create_reminder.adapter.ColorSpinnerAdapter
import com.example.reminderapp.presentation.create_reminder.adapter.GroupSpinnerAdapter
import com.example.reminderapp.presentation.create_reminder.adapter.PeriodSpinnerAdapter
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants.GROUP_KEY
import com.example.reminderapp.presentation.new_list.NewListFragment
import com.example.reminderapp.utils.actionIfChanged
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.Locale


class CreateReminderFragment : Fragment(), MenuProvider, BackActionInterface, DataReceiving {

    companion object {
        fun newInstance() = CreateReminderFragment()
    }

    private var _binding: FragmentCreateReminderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateReminderViewModel by viewModel()

    private lateinit var groupSpinnerAdapter: GroupSpinnerAdapter
    private lateinit var periodSpinnerAdapter: PeriodSpinnerAdapter
    private lateinit var colorSpinnerAdapter: ColorSpinnerAdapter

    val dateFormat = SimpleDateFormat("E, dd.MM.yyyy 'г.' HH:mm", Locale.getDefault())

    var hasNotificationPermisions = false
    var hasScheduleExactAlarmPermisions = false


    private lateinit var callback: OnBackPressedCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReminderBinding.inflate(layoutInflater, container, false)

        setFragmentResultListener(GROUP_KEY) { key, bundle ->
            viewModel.onGroupIdChanged(bundle.getLong(key).toInt())
        }
        checkPermissions()

        edgeToEdge()
        setupToolbar()
        setupOnBackPressed()

        initListeners()
        setupSwitches()
        setupSpinnerColor()
        setupSpinnerPeriod()
        setSpinnerGroup()
        setupObservers()
        viewModel.fetchGroups()


        binding.selectedDateTv.setOnClickListener {
            showDateAndTimePickers()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }


    override fun receiveData() {
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
            val selectedGroupName = it.getString(FragmentNavigationConstants.LIST_NAME)
            viewModel.onGroupNameChanged(selectedGroupName)
            if (taskSerialized != null) {
                viewModel.onTaskIdChanged(taskSerialized.id)
                viewModel.onFirstTimeChanged(taskSerialized.reminderTime)
                viewModel.onPeriodChanged(taskSerialized.reminderTimePeriod)
                viewModel.onColorChanged(taskSerialized.color)
                viewModel.onGroupIdChanged(taskSerialized.groupId)
                viewModel.onNameTextChanged(taskSerialized.name)
                viewModel.onDescriptionTextChanged(taskSerialized.description)
                viewModel.onFlagChanged(taskSerialized.isMarkedWithFlag)
                if(taskSerialized.reminderTime!=null || taskSerialized.reminderTimePeriod!=null) {
                    viewModel.onRemindSwitchChecked(true)
                }
            }
        }
    }

    private fun initListeners() {
        binding.reminderNameEt.doAfterTextChanged(viewModel::onNameTextChanged)
        binding.reminderDescriptionEt.doAfterTextChanged(viewModel::onDescriptionTextChanged)
    }
    private fun setupSwitches() {
        binding.remindSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onRemindSwitchChecked(isChecked)
            val constraintLayout = binding.dateContainer
            val linearLayout = binding.dateAndPeriodLl
            if (isChecked) {
                val layoutParams = linearLayout.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = 0
                linearLayout.layoutParams = layoutParams
                val animator = ValueAnimator.ofInt(0, getContentHeight(binding.dateAndPeriodLl)).apply {
                    duration = 500
                    addUpdateListener { animation ->
                        val value = animation.animatedValue as Int
                        layoutParams.height = value
                        linearLayout.layoutParams = layoutParams
                        val y: Float = constraintLayout.height.toFloat() - linearLayout.height.toFloat()
                        linearLayout.y = y
                    }
                }
                animator.start()
            }
            else {
                val layoutParams = linearLayout.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = getContentHeight(binding.dateAndPeriodLl)
                linearLayout.layoutParams = layoutParams
                val animator = ValueAnimator.ofInt(getContentHeight(binding.dateAndPeriodLl), 0).apply {
                    duration = 500 // длительность анимации в миллисекундах
                    addUpdateListener { animation ->
                        val value = animation.animatedValue as Int
                        layoutParams.height = value
                        linearLayout.layoutParams = layoutParams
                        val y: Float = constraintLayout.height.toFloat() - linearLayout.height.toFloat()
                        linearLayout.y = y
                    }
                }

                animator.start()
            }
        }

        binding.flagSwitch.setOnCheckedChangeListener {  _, isChecked ->
            viewModel.onFlagChanged(isChecked)
        }
    }

    private fun setupObservers() {
        observeScreenState()
        observeValidation()
        observeOperationResult()
    }

    private fun observeOperationResult() {
        lifecycleScope.launch {
            viewModel.saveResult.collect { saveResult ->
                when (saveResult) {
                    is OperationResult.Error -> showSnackbar(
                        saveResult.message,
                        requireActivity().findViewById(R.id.rootView)
                    )

                    OperationResult.Loading -> {
                        binding.contentLayout.isVisible = false
                        binding.loadingLayout.isVisible = true
                    }

                    OperationResult.NotStarted -> Unit
                    is OperationResult.Success -> {
                        navigateBack()
                    }
                }
            }
        }
    }

    private fun observeValidation() {
        lifecycleScope.launch {
            viewModel.validationResult.collect { validationResult ->
                when (validationResult) {
                    ValidationResult.IncorrectGroup -> {
                        showSnackbar(
                            getString(R.string.group_should_be_selected),
                            requireActivity().findViewById(R.id.rootView)
                        )
                    }

                    ValidationResult.IncorrectName -> {
                        binding.reminderNameEt.setFocus(requireContext())
                        binding.reminderNameEt.setError(getString(R.string.name_shouldnt_be_empty))
                    }

                    ValidationResult.NotStarted -> Unit
                    ValidationResult.IncorrectColor -> {
                        showSnackbar(
                            getString(R.string.color_should_be_selected),
                            requireActivity().findViewById(R.id.rootView)
                        )
                    }
                }
            }
        }
    }

    private fun observeScreenState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState
                    .collect { currentState ->
                    when (currentState.groupsUiState) {
                        is UiState.Success -> {
                            binding.contentLayout.isVisible = true
                            binding.loadingLayout.isVisible = false
                            with(binding) {
                                reminderNameEt.actionIfChanged(currentState.reminderName) {
                                    reminderNameEt.setText(currentState.reminderName)
                                    reminderNameEt.setSelection(currentState.reminderName.length)
                                }
                                reminderDescriptionEt.actionIfChanged(currentState.reminderDescription) {
                                    reminderDescriptionEt.setText(currentState.reminderDescription)
                                    reminderDescriptionEt.setSelection(currentState.reminderDescription.length)
                                }
                                remindSwitch.actionIfChanged(currentState.reminderPeriod!=null || currentState.reminderFirstTime!=null) {
                                    remindSwitch.isChecked = currentState.reminderPeriod!=null || currentState.reminderFirstTime!=null
                                }
                                selectedDateTv.actionIfChanged(currentState.reminderFirstTime) {
                                    selectedDateTv.text = if(currentState.reminderFirstTime!=null)dateFormat.format(currentState.reminderFirstTime) else requireContext().getString(R.string.not_selected)
                                }
                                selectedPeriodSpinner.actionIfChanged(currentState.reminderPeriod) {
                                    selectedPeriodSpinner.setSelection(periodSpinnerAdapter.getTimePeriodPosition(currentState.reminderPeriod))
                                }
                                selectedColorSpinner.actionIfChanged(currentState.reminderColor) {
                                    selectedColorSpinner.setSelection(colorSpinnerAdapter.getColorPosition(currentState.reminderColor))
                                }
                                if (!currentState.groupLoaded){
                                    groupSpinnerAdapter.submitList(currentState.groupsUiState.data)
                                    viewModel.onGroupLoadedToSpinner()
                                }
                                if (currentState.reminderGroupId!=null){
                                    selectedListSpinner.actionIfChanged(currentState.reminderGroupId) {
                                        selectedListSpinner.setSelection(groupSpinnerAdapter.getGroupPosition(currentState.reminderGroupId))
                                    }
                                }
                                else {
                                    selectedListSpinner.actionIfChanged(currentState.reminderGroupName) {
                                        selectedListSpinner.setSelection(groupSpinnerAdapter.getGroupPosition(currentState.reminderGroupName))
                                    }
                                }
                            }
                        }
                        is UiState.Loading -> {
                            binding.contentLayout.isVisible = false
                            binding.loadingLayout.isVisible = true
                        }
                        is UiState.Error -> {
                            binding.contentLayout.isVisible = true
                            binding.loadingLayout.isVisible = false
                            showSnackbar(currentState.groupsUiState.message, requireActivity().findViewById(R.id.rootView))
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
    private fun setupSpinnerPeriod() {
        val timeDates = TimeDateUtils(requireContext()).timeDates

        periodSpinnerAdapter = PeriodSpinnerAdapter(
            requireContext(),
            onNoneSelected = {
                viewModel.onPeriodChanged(null)
            },
            onItemSelected = { period ->
                viewModel.onPeriodChanged(period.time)
            }
        )

        val spinner = binding.selectedPeriodSpinner
        spinner.adapter = periodSpinnerAdapter

        binding.selectedPeriodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                periodSpinnerAdapter.handleItemSelected(pos)
            }
        }
        periodSpinnerAdapter.submitList(timeDates)
    }

    private fun setSpinnerGroup() {

        groupSpinnerAdapter = GroupSpinnerAdapter(
            requireContext(),
            onNoneSelected = {
                viewModel.onGroupIdChanged(null)
            },
            onCreateGroup = {
                navigateToNewGroupScreen()
            },
            onGroupSelected = { group ->
                viewModel.onGroupIdChanged(group.groupId)
            }
        )
        val spinner = binding.selectedListSpinner
        spinner.adapter = groupSpinnerAdapter

        binding.selectedListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                groupSpinnerAdapter.handleItemSelected(pos)
            }
        }
    }

    private fun setupSpinnerColor() {
        val colorItems = ColorsUtils(requireContext()).colors

        colorSpinnerAdapter = ColorSpinnerAdapter(
            requireContext(),
            onItemSelected = { color ->
                viewModel.onColorChanged(color.color)
            }
        )

        val spinner = binding.selectedColorSpinner
        spinner.adapter = colorSpinnerAdapter

        binding.selectedColorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                colorSpinnerAdapter.handleItemSelected(pos)
            }
        }
        colorSpinnerAdapter.submitList(colorItems)
    }
    //TODO переделать
    private fun saveTask() {
        if(hasNotificationPermisions && hasScheduleExactAlarmPermisions) {
            viewModel.saveTask()
        }
        else {
            checkPermissions()
        }
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

                        viewModel.onFirstTimeChanged(selectedDate.time.time)
                    },
                    getDate.get(Calendar.HOUR_OF_DAY),
                    getDate.get(Calendar.MINUTE),
                    true
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

    private fun navigateToNewGroupScreen() {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_anim,
                R.anim.slide_out_anim,
                R.anim.slide_in_anim,
                R.anim.slide_out_anim
            )
            .replace(
                R.id.fragmentContainerView,
                NewListFragment()
            )
            .addToBackStack(FragmentNavigationConstants.TASK_KEY)
            .commit()
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

}
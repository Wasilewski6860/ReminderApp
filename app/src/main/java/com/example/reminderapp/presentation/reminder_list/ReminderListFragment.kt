package com.example.reminderapp.presentation.reminder_list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Task
import com.example.reminderapp.MainActivity
import com.example.reminderapp.databinding.FragmentReminderListBinding
import com.example.reminderapp.presentation.base.OperationResult
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.base.DataReceiver
import com.example.reminderapp.presentation.base.NavigationFragment
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants
import com.example.reminderapp.presentation.navigation.NavigationDestinations
import com.example.reminderapp.presentation.navigation.TasksListTypeCase
import com.example.reminderapp.presentation.reminder_list.adapter.ReminderItemDivider
import com.example.reminderapp.presentation.reminder_list.adapter.ReminderListAdapter
import com.example.reminderapp.utils.setPaddingToInset
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : NavigationFragment(), DataReceiver, MenuProvider {

    override val backstackTag: String = FragmentNavigationConstants.TASK_KEY

    private var _binding: FragmentReminderListBinding? = null
    private val binding get() = _binding!!
    private lateinit var reminderAdapter: ReminderListAdapter

    private val viewModel: ReminderListViewModel by viewModel()

    private var groupName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderListBinding.inflate(layoutInflater, container, false)

        edgeToEdge()
        setupToolbar()

        setupOnBackPressed()

        // viewModel.fetchData(groupType)
        setupRecyclerView()
        setupObservers()
        initListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner , Lifecycle.State.RESUMED)
    }


    private fun setupToolbar() = with(binding) {
        val activity = (activity as MainActivity)
        activity.setSupportActionBar(reminderListToolbar)

        activity.supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun edgeToEdge() = with(binding) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
            reminderListToolbar setPaddingToInset WindowInsetsCompat.Type.statusBars()
        }
    }

    private fun initListener() = with(binding) {
        addFloatingActionButton.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(
                    FragmentNavigationConstants.LIST_NAME,
                    groupName
                )
            }
            navigateTo(NavigationDestinations.ToCreateReminderFragment,bundle)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.contentLayout.isVisible = true
                            binding.loadingLayout.isVisible = false
                            binding.nothingFindLayout.isVisible = false

                            reminderAdapter.submitList(it.data.tasks)
                            groupName = it.data.groupName
                            binding.reminderListToolbar.title = it.data.groupName
                        }
                        is UiState.Loading -> {
                            binding.contentLayout.isVisible = false
                            binding.loadingLayout.isVisible = true
                            binding.nothingFindLayout.isVisible = false
                        }
                        is UiState.Error -> {
                            binding.contentLayout.isVisible = false
                            binding.loadingLayout.isVisible = false
                            binding.nothingFindLayout.isVisible = true
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.operationResult.collect {
                when (it) {
                    is OperationResult.Error -> Unit
                    OperationResult.Loading -> {
                        binding.contentLayout.isVisible = false
                        binding.loadingLayout.isVisible = true
                        binding.nothingFindLayout.isVisible = false
                    }
                    OperationResult.NotStarted -> Unit
                    is OperationResult.Success -> {
                        binding.contentLayout.isVisible = true
                        binding.loadingLayout.isVisible = false
                        binding.nothingFindLayout.isVisible = false
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
        reminderAdapter = ReminderListAdapter(
            onItemClick = object : ReminderListAdapter.OnClickListener {
                override fun onItemClick(task: Task) {
                    val bundle = Bundle().apply {
                        putSerializable(
                            FragmentNavigationConstants.TASK_KEY,
                            task
                        )
                    }
                    navigateTo(NavigationDestinations.ToCreateReminderFragment ,bundle)
                }
                override fun onSwitchClick(task: Task, isChecked: Boolean) {
                    viewModel.editIsActive(
                        task = task,
                        isActive = isChecked
                    )
                }

                override fun onDeleteClick(task: Task) {
                    viewModel.deleteTask(task) // TODO check this method in viewModel
                }
            }
        )
        adapter = reminderAdapter
        layoutManager = LinearLayoutManager(requireContext())
        this.addItemDecoration(ReminderItemDivider(requireContext()))
    }

    override fun receiveData() {
        arguments?.let {
            val taskTypeSerialized: TasksListTypeCase? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(
                    FragmentNavigationConstants.LIST_TYPE, TasksListTypeCase::class.java
                )
            } else {
                it.getSerializable(FragmentNavigationConstants.LIST_TYPE) as TasksListTypeCase?
            }
            taskTypeSerialized?.let { data ->
                viewModel.fetchData(data)
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) = Unit

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                navigateBack()
                return true
            }
        }
        return true
    }

}
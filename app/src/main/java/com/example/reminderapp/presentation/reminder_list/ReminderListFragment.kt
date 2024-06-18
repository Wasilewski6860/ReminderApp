package com.example.reminderapp.presentation.reminder_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Task
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.databinding.FragmentReminderListBinding
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.base.serializer.GroupSerializer
import com.example.reminderapp.presentation.base.serializer.TaskSerializer
import com.example.reminderapp.presentation.base.serializer.TasksListTypeCaseSerializer
import com.example.reminderapp.presentation.create_reminder.CreateReminderFragment
import com.example.reminderapp.presentation.interfaces.DataReceiving
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants
import com.example.reminderapp.presentation.navigation.TasksListTypeCase
import com.example.reminderapp.presentation.new_list.NewListFragment
import com.example.reminderapp.reminder.RemindAlarmManager
import com.example.reminderapp.reminder.work.RemindWorkManager
import com.example.reminderapp.utils.Constants.GROUP_KEY
import com.example.reminderapp.utils.Constants.TASK_KEY
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : Fragment(), DataReceiving {

    companion object {
        fun newInstance() = ReminderListFragment()
    }

    private var _binding: FragmentReminderListBinding? = null
    private val binding get() = _binding!!
    private lateinit var reminderAdapter: ReminderListAdapter

    private val remindAlarmManager: RemindAlarmManager by inject()
    private val remindWorkManager: RemindWorkManager by inject()

    private val viewModel: ReminderListViewModel by viewModel()

    private var groupId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderListBinding.inflate(layoutInflater, container, false)


        // viewModel.fetchData(groupType)
        setupRecyclerView()
        setupObservers()



        return binding.root
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.contentLayout.visibility = View.VISIBLE
                            binding.loadingLayout.visibility = View.GONE
                            binding.nothingFindLayout.visibility = View.GONE

                            reminderAdapter.submitList(it.data.tasks)
                            (activity as MainActivity).setToolbarTitleAndTitleColor(it.data.groupName)
                        }
                        is UiState.Loading -> {
                            binding.contentLayout.visibility = View.GONE
                            binding.loadingLayout.visibility = View.VISIBLE
                            binding.nothingFindLayout.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            binding.contentLayout.visibility = View.GONE
                            binding.loadingLayout.visibility = View.GONE
                            binding.nothingFindLayout.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
        reminderAdapter = ReminderListAdapter(
            onItemClickListener = object : ReminderListAdapter.OnItemClickListener {
                override fun onClickItem(task: Task) {
                    val bundle = Bundle().apply {
                        putSerializable(
                            FragmentNavigationConstants.TASK_KEY,
                            task
                        )
                    }
                    navigateToEditReminder(bundle)
                }
            },
            onSwitchClickListener = object : ReminderListAdapter.OnSwitchClickListener {
                override fun onClickItem(task: Task, isChecked: Boolean) {
                    if (isChecked) {
                        remindAlarmManager.createAlarm(task)
                    }
                    else {
                        remindWorkManager.createCancelWorkRequest(taskId = task.id)
                    }
                }
            }
        )
        adapter = reminderAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun navigateToEditReminder(args: Bundle) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_anim,
                R.anim.slide_out_anim,
                R.anim.slide_in_anim,
                R.anim.slide_out_anim
            )
            .replace(
                R.id.fragmentContainerView,
                CreateReminderFragment().apply {
                    args?.let { arguments = it }
                }
            )
            .addToBackStack(FragmentNavigationConstants.TASK_KEY)
            .commit()
    }

    override fun receiveData() {
        arguments?.let {
            val taskTypeSerialized: TasksListTypeCase? = it.getSerializable(FragmentNavigationConstants.LIST_TYPE)  as TasksListTypeCase?
            taskTypeSerialized?.let { data ->
                viewModel.fetchData(data)
            }
        }
    }

}
package com.example.reminderapp.presentation.reminder_list

import android.os.Bundle
import android.util.Log
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
import com.example.reminderapp.presentation.create_reminder.CreateReminderFragment
import com.example.reminderapp.presentation.interfaces.DataReceiving
import com.example.reminderapp.presentation.navigation.TasksListTypeCase
import com.example.reminderapp.reminder.RemindAlarmManager
import com.example.reminderapp.reminder.work.RemindWorkManager
import com.example.reminderapp.utils.Constants
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
    lateinit var groupType: TasksListTypeCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        groupId = arguments?.getInt(GROUP_KEY) ?: -1

//        reminderGroup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arguments?.getSerializable(GROUP_KEY, Group::class.java)
//        }
//        else {
//            arguments?.getSerializable(GROUP_KEY) as Group?
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderListBinding.inflate(layoutInflater, container, false)

        viewModel.fetchData(groupType)
        setupRecyclerView()
        setupObservers()

        receiveData()

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
                    val fragmentManager = requireActivity().supportFragmentManager
                    val bundle = Bundle()
                    bundle.putSerializable(TASK_KEY, task)
                    val fragmentTransaction = fragmentManager.beginTransaction()

                    val targetFragment = CreateReminderFragment()
                    targetFragment.arguments = bundle

                    fragmentTransaction.replace(R.id.fragmentContainerView, targetFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
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

    override fun receiveData() {
        arguments?.let {
            // TODO data receiving here
            Log.d("Test", it.getInt(Constants.GROUP_ID).toString())
        }
    }

}
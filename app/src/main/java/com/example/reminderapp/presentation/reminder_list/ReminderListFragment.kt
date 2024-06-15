package com.example.reminderapp.presentation.reminder_list

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.databinding.FragmentCreateReminderBinding
import com.example.reminderapp.databinding.FragmentReminderListBinding
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.create_reminder.CreateReminderFragment
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.example.reminderapp.presentation.recycleradapter.GroupListRecyclerViewAdapter
import com.example.reminderapp.reminder.RemindAlarmManager
import com.example.reminderapp.reminder.work.RemindWorkManager
import com.example.reminderapp.utils.Constants.GROUP_KEY
import com.example.reminderapp.utils.Constants.TASK_KEY
import com.example.reminderapp.utils.showSnackbar
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : Fragment() {

    companion object {
        fun newInstance() = ReminderListFragment()
    }

    private var _binding: FragmentReminderListBinding? = null
    private val binding get() = _binding!!
    private lateinit var reminderAdapter: ReminderListAdapter

    private val remindAlarmManager: RemindAlarmManager by inject()
    private val remindWorkManager: RemindWorkManager by inject()

    private val viewModel: ReminderListViewModel by viewModel()

    var groupId: Int = -1

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
    ): View? {
        _binding = FragmentReminderListBinding.inflate(layoutInflater, container, false)

        val activity = (activity as MainActivity)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.fetchGroupWithTasks(groupId)
        setupRecyclerView()
        setupObservers()
    }

    fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.contentLayout.visibility = View.VISIBLE
                            binding.loadingLayout.visibility = View.GONE

                            reminderAdapter.submitList(it.data.tasks)
                            it.data.group.apply {
                                val activity = (activity as MainActivity)
                                activity.setToolbarTitleAndTitleColor(this.groupName, this.groupColor)
                            }
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

}
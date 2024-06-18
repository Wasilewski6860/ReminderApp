package com.example.reminderapp.presentation.mainscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.animations.animateImageViewRotation
import com.example.reminderapp.animations.hideRecyclerAnimation
import com.example.reminderapp.animations.showRecyclerAnimation
import com.example.reminderapp.databinding.MainScreenBinding
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.create_reminder.CreateReminderFragment
import com.example.reminderapp.presentation.editorlistsscreen.EditListsScreenFragment
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants
import com.example.reminderapp.presentation.navigation.TasksListTypeCase
import com.example.reminderapp.presentation.navigation.TasksListTypeCaseSerializer
import com.example.reminderapp.presentation.new_list.NewListFragment
import com.example.reminderapp.presentation.recycleradapter.GroupListRecyclerViewAdapter
import com.example.reminderapp.presentation.reminder_list.ReminderListFragment
import com.example.reminderapp.utils.Constants
import com.example.reminderapp.utils.showSnackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: MainScreenBinding
    private lateinit var adapter: GroupListRecyclerViewAdapter
    private val viewModel by viewModel<MainViewModel>()

    private var currentArrowRotation = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainScreenBinding.inflate(inflater, container, false)

        (activity as MainActivity).setToolbarTitleAndTitleColor("")

        binding.apply {
            setupRecyclerAndAdapter()
            setupObserver()
            initListeners()
        }

        initListeners()

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initListeners() = with(binding) {
        listTitleHolder.setOnClickListener {
            toggleImageRotation(showRecyclerViewButton)
            when (customListsRecyclerView.visibility) {
                View.VISIBLE -> {
                    hideRecyclerAnimation(customListsRecyclerView)
                }

                View.GONE -> {
                    showRecyclerAnimation(customListsRecyclerView)
                }

                else -> {}
            }
        }

        addTaskButton.setOnClickListener {
            navigate(Navigation.ToCreateReminderFragment)
        }

        changeListsButton.setOnClickListener {
            navigate(Navigation.ToEditListsFragment)
        }

        addListButton.setOnClickListener {
            navigate(Navigation.ToNewListFragment)
        }
    }

    private fun navigate(destination: Navigation, args: Bundle? = null) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_anim,
                R.anim.slide_out_anim,
                R.anim.slide_in_anim,
                R.anim.slide_out_anim
            )
            .replace(
                R.id.fragmentContainerView,
                when (destination) {
                    Navigation.ToCreateReminderFragment -> CreateReminderFragment()
                    Navigation.ToNewListFragment -> NewListFragment()
                    Navigation.ToEditListsFragment -> EditListsScreenFragment()
                    Navigation.ToReminderListFragment -> ReminderListFragment()
                }
            )
            .apply {
                args?.let { arguments = it }
            }
            .addToBackStack(FragmentNavigationConstants.TO_MAIN_FRAGMENT_BACKSTACK)
            .commit()
    }

    private fun gridLayoutItemsInit(todayCount: Int, plannedCount: Int, withFlagCount: Int) = with(binding) {
        topGridLayout.apply {
            currentDayTasksItem.apply {
                counterTitle = todayCount.toString()
                setOnClickListener {
                    val bundle = Bundle().apply {
                       putString(
                           FragmentNavigationConstants.LIST_TYPE,
                           TasksListTypeCaseSerializer.serialize(TasksListTypeCase.TodayTasks)
                           )
                    }
                    navigate(Navigation.ToReminderListFragment, bundle)
                }
            }
            plannedTasksItem.apply {
                counterTitle = plannedCount.toString()
                setOnClickListener {
                    val bundle = Bundle().apply {
                        putString(
                            FragmentNavigationConstants.LIST_TYPE,
                            TasksListTypeCaseSerializer.serialize(TasksListTypeCase.PlannedTasks)
                        )
                    }
                    navigate(Navigation.ToReminderListFragment, bundle)
                }
            }
            allTasksItem.apply {
                counterTitle = plannedCount.toString() // TODO replace this with all tasks title later
                setOnClickListener {
                    val bundle = Bundle().apply {
                        putString(
                            FragmentNavigationConstants.LIST_TYPE,
                            TasksListTypeCaseSerializer.serialize(TasksListTypeCase.AllTasks)
                        )
                    }
                    navigate(Navigation.ToReminderListFragment, bundle)
                }
            }
            tasksWithFlagItem.apply {
                counterTitle = withFlagCount.toString()
                setOnClickListener {
                    val bundle = Bundle().apply {
                        putString(
                            FragmentNavigationConstants.LIST_TYPE,
                            TasksListTypeCaseSerializer.serialize(TasksListTypeCase.TasksWithFlag)
                        )
                    }
                    navigate(Navigation.ToReminderListFragment, bundle)
                }
            }
        }
    }

    private fun toggleImageRotation(imageView: ImageView) {
        currentArrowRotation += 180f
        if (currentArrowRotation > 360f) {
            currentArrowRotation -= 360f
        }
        animateImageViewRotation(imageView, currentArrowRotation)
    }

    private fun setupRecyclerAndAdapter() = with(binding) {
        adapter = GroupListRecyclerViewAdapter(object :
            GroupListRecyclerViewAdapter.OnItemClickListener {
            override fun onRcItemClick(position: Int) {
                val bundle = Bundle().apply {
                    putString(
                        FragmentNavigationConstants.LIST_TYPE,
                        TasksListTypeCaseSerializer.serialize(TasksListTypeCase.GroupTasks(
                            adapter.getGroupId(position)
                        ))
                    )
                }
                navigate(Navigation.ToReminderListFragment, bundle)
            }
            override fun onDeleteIconClick(position: Int) {
                /** STUB **/
            }
        }, isDeleteIconVisible = false)

        customListsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        customListsRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.fetchData()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.contentLayout.visibility = View.VISIBLE
                            binding.loadingLayout.visibility = View.GONE

                            it.data.apply {
                                adapter.fillRecyclerWithFullItemsList(this.groups)
                                gridLayoutItemsInit(
                                    todayCount = this.todayCount,
                                    plannedCount = this.plannedCount,
                                    withFlagCount = this.withFlagCount
                                )
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

}

private sealed class Navigation {

    object ToCreateReminderFragment : Navigation()

    object ToEditListsFragment : Navigation()

    object ToNewListFragment : Navigation()

    object ToReminderListFragment : Navigation()

}
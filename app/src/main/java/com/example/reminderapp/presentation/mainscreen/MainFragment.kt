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
import com.example.domain.model.Group
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.animations.animateImageViewRotation
import com.example.reminderapp.animations.hideRecyclerAnimation
import com.example.reminderapp.animations.showRecyclerAnimation
import com.example.reminderapp.databinding.MainScreenBinding
import com.example.reminderapp.presentation.create_reminder.CreateReminderFragment
import com.example.reminderapp.presentation.editorlistsscreen.EditListsScreenFragment
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants
import com.example.reminderapp.presentation.new_list.NewListFragment
import com.example.reminderapp.presentation.recycleradapter.GroupListRecyclerViewAdapter
import com.example.reminderapp.presentation.reminder_list.ReminderListFragment
import com.example.reminderapp.utils.Constants
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
            gridLayoutItemsInit()

            /** For testing */
            adapter.fillRecyclerWithFullItemsList(Test.getTestList())

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

    private fun gridLayoutItemsInit() = with(binding) {
        topGridLayout.apply {
            currentDayTasksItem.apply {
                counterTitle = viewModel.todayReminders
                setOnClickListener {
                    val bundle = Bundle().apply {

                    }
                    navigate(Navigation.ToReminderListFragment)
                }
            }
            plannedTasksItem.apply {
                counterTitle = viewModel.plannedReminders
                setOnClickListener {
                    val bundle = Bundle().apply {

                    }
                    navigate(Navigation.ToReminderListFragment)
                }
            }
            allTasksItem.apply {
                counterTitle = viewModel.allReminders
                setOnClickListener {
                    val bundle = Bundle().apply {

                    }
                    navigate(Navigation.ToReminderListFragment)
                }
            }
            tasksWithFlagItem.apply {
                counterTitle = viewModel.remindersWithFlag
                setOnClickListener {
                    val bundle = Bundle().apply {

                    }
                    navigate(Navigation.ToReminderListFragment)
                }
            }
            // TODO add today tasks data collection method in viewModel
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
                /** Transition on TasksListFragment */
                /** And add data to this transaction */
                val bundle = Bundle().apply {
                    putInt(Constants.GROUP_ID, adapter.getGroupId(position))
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
        viewModel.fetchTaskGroups()
        viewModel.getAllTasks()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupsListData.collect {
                    adapter.fillRecyclerWithFullItemsList(it)
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

private object Test {
    fun getTestList(): List<Group> {
        return listOf(
            Group(
                groupId = 0,
                groupName = "First list",
                groupColor = R.color.red
            ),
            Group(
                groupId = 1,
                groupName = "Second list",
                groupColor = R.color.black
            ),
            Group(
                groupId = 2,
                groupName = "Third list",
                groupColor = R.color.blue
            ),
            Group(
                groupId = 3,
                groupName = "Fourth list",
                groupColor = R.color.purple_500
            )
        )
    }
}
package com.example.reminderapp.presentation.mainscreen

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.model.Group
import com.example.reminderapp.R
import com.example.reminderapp.animations.animateImageViewRotation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.databinding.MainScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: MainScreenBinding
    private lateinit var adapter: MainScreenRecyclerViewAdapter
    private val viewModel by viewModel<MainViewModel>()

    private var currentArrowRotation = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainScreenBinding.inflate(inflater, container, false)

        binding.apply {

            adapter = MainScreenRecyclerViewAdapter(object :
                MainScreenRecyclerViewAdapter.OnItemClickListener {
                override fun onRcItemClick(position: Int) {
                    /** Transition on CreateReminderFragment */
                    /** And add data to this transaction */
                }
            })

            customListsRecyclerView.layoutManager = GridLayoutManager(context, 1)
            customListsRecyclerView.adapter = adapter

            viewModel.fetchTaskGroups()
            viewModel.groupsListData.observe(viewLifecycleOwner) {
                adapter.fillRecyclerWithFullItemsList(it)
            }

            adapter.fillRecyclerWithFullItemsList(
                Test.getTestList()
            )
        setupRecyclerAndAdapter()
        setupObserver()
        initListeners()
        gridLayoutItemsInit()

        /** For testing */
        adapter.fillRecyclerWithFullItemsList(
            Test.getTestList()
        )

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
    }

    private fun gridLayoutItemsInit() = with(binding) {
        topGridLayout.apply {
            currentDayTasksItem.setOnClickListener {

            }
            plannedTasksItem.setOnClickListener {

            }
            allTasksItem.setOnClickListener {

            }
            tasksWithFlagItem.setOnClickListener {

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
        adapter = MainScreenRecyclerViewAdapter(object :
            MainScreenRecyclerViewAdapter.OnItemClickListener {
            override fun onRcItemClick(position: Int) {
                /** Transition on CreateReminderFragment */
                /** And add data to this transaction */
            }
        })

        customListsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        customListsRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.fetchTaskGroups()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupsListData.collect {
                    adapter.fillRecyclerWithFullItemsList(it)
                }
            }
        }
    }

    private fun hideRecyclerAnimation(recyclerView: RecyclerView) {
        val alphaAnimation = ObjectAnimator.ofFloat(
            recyclerView, "alpha", 1f, 0f
        )
        val translateXAnimation = ObjectAnimator.ofFloat(
            recyclerView, "translationX", 0f, -recyclerView.width.toFloat()
        )

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(alphaAnimation, translateXAnimation)

        animatorSet.duration = 500

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                recyclerView.visibility = View.GONE
            }
        })

        animatorSet.start()
    }

    private fun showRecyclerAnimation(recyclerView: RecyclerView) {
        val alphaAnimation = ObjectAnimator.ofFloat(
            recyclerView, "alpha", 0f, 1f
        )
        val translateXAnimation = ObjectAnimator.ofFloat(
            recyclerView, "translationX", recyclerView.height.toFloat(), 0f
        )

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(alphaAnimation, translateXAnimation)

        animatorSet.duration = 500

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                recyclerView.visibility = View.VISIBLE
            }
        })

        animatorSet.start()
    }

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
                groupId = 0,
                groupName = "Second list",
                groupColor = R.color.black
            ),
            Group(
                groupId = 0,
                groupName = "Third list",
                groupColor = R.color.blue
            ),
            Group(
                groupId = 0,
                groupName = "Fourth list",
                groupColor = R.color.purple_500
            )
        )
    }
}
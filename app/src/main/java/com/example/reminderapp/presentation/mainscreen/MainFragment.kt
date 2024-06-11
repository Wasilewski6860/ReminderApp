package com.example.reminderapp.presentation.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.R
import com.example.reminderapp.databinding.MainScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: MainScreenBinding
    private lateinit var adapter: MainScreenRecyclerViewAdapter
    private val viewModel by viewModel<MainViewModel>()

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
                }
            })

            customListsRecyclerView.layoutManager = GridLayoutManager(context, 1)
            customListsRecyclerView.adapter = adapter

            viewModel.fetchTaskGroups()
            viewModel.tasksListData.observe(viewLifecycleOwner) {
                adapter.fillRecyclerWithFullItemsList(it)
            }

        }

        initListeners()

        return binding.root
    }

    private fun initListeners() = with(binding) {
        showRecyclerViewButton.setOnClickListener {
            val currentImageId = R.drawable.arrow_drop_down

            when (currentImageId) {
                R.drawable.arrow_drop_down -> {
                    showRecyclerViewButton.setImageResource(R.drawable.arrow_drop_up)
                    customListsRecyclerView.visibility = View.GONE
                }

                else -> {
                    showRecyclerViewButton.setImageResource(R.drawable.arrow_drop_down)
                    customListsRecyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

}
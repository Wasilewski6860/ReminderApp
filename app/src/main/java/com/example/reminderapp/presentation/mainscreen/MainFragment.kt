package com.example.reminderapp.presentation.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.databinding.MainFragmentBinding
import com.example.reminderapp.presentation.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), MainScreenRecyclerViewAdapter.OnItemClickListener {

    private lateinit var binding: MainFragmentBinding
    private val adapter = MainScreenRecyclerViewAdapter(this)
    private val viewModel by viewModel<MainViewModel>()
    private val sharedViewModel by viewModel<SharedViewModel>()

    init {
        // LiveData observing here (for now one i guess)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        // viewModel.getAllTasks() <-- ??

        binding.apply {

            mainScreenRecyclerView.layoutManager = GridLayoutManager(context, 1)
            mainScreenRecyclerView.adapter = adapter

            viewModel.getTasksListLiveData().observe(viewLifecycleOwner) { itemsList ->
                // Recycler view adding element process here
                adapter.fillRecyclerWithFullItemsList(itemsList)
            }

        }

        return binding.root
    }

    override fun onRcItemClick(position: Int) {
        // Transition on creator screen with current rcItem data to edit it
    }

}
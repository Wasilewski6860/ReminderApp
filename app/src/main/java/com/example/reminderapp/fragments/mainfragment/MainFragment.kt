package com.example.reminderapp.fragments.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.databinding.MainFragmentBinding
import com.example.reminderapp.fragments.mainfragment.recyclerview.MainScreenRecyclerViewAdapter
import com.example.reminderapp.viewmodels.mainscreen.MainViewModel

class MainFragment : Fragment(), MainScreenRecyclerViewAdapter.OnItemClickListener {

    private lateinit var binding: MainFragmentBinding
    private val adapter = MainScreenRecyclerViewAdapter(this)

    private lateinit var viewModel: MainViewModel // solution without di

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java] // temp solution
        // viewModel.getAllTasks() <-- ??

        binding.apply {

            // Not sure about context here tbh (may be we can transmit context from activity)
            mainScreenRecyclerView.layoutManager = GridLayoutManager(context, 1)
            mainScreenRecyclerView.adapter = adapter

            // idk about 'viewLifecycleOwner'
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
package com.example.reminderapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.reminderapp.databinding.MainFragmentBinding
import com.example.reminderapp.viewmodels.mainscreen.MainViewModel

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

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

            // idk about 'viewLifecycleOwner'
            viewModel.getTasksListLiveData().observe(viewLifecycleOwner) {
                // Recycler view adding element process here (need create Adapter class for this)
            }

        }

        return binding.root
    }

}
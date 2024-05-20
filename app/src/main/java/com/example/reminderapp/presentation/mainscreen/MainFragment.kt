package com.example.reminderapp.presentation.mainscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.databinding.MainFragmentBinding
import com.example.reminderapp.notification.Constants
import com.example.reminderapp.notification.Constants.DELETE_TASK
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), MainScreenRecyclerViewAdapter.OnItemClickListener {

    private lateinit var binding: MainFragmentBinding
    private val adapter = MainScreenRecyclerViewAdapter(this)
    private val viewModel by viewModel<MainViewModel>()

    private val deleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val bundle = intent?.extras ?: return
            val id = bundle.getInt("id")
            viewModel.deleteTask(id)
        }
    }
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

        viewModel.getAllTasks()
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

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(DELETE_TASK)
        context?.registerReceiver(deleteReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(deleteReceiver)
    }
}
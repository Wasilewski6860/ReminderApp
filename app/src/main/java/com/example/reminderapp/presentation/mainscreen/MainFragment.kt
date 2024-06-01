package com.example.reminderapp.presentation.mainscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.R
import com.example.reminderapp.databinding.MainFragmentBinding
import com.google.gson.Gson
import com.example.reminderapp.notification.Constants
import com.example.reminderapp.notification.Constants.DELETE_TASK
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: MainScreenRecyclerViewAdapter
    private val viewModel by viewModel<MainViewModel>()

    init {
        // LiveData observing here (for now one i guess)
    }

    companion object {
        const val TASK_DATA = "data"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.apply {

            adapter = MainScreenRecyclerViewAdapter(object : MainScreenRecyclerViewAdapter.OnItemClickListener {
                override fun onRcItemClick(position: Int) {
                    // Transition on creator screen here
                    val data = Gson().toJson(adapter.getItemByPosition(position))
                    val bundle = Bundle().apply {
                        putString(TASK_DATA, data)
                    }
                    val action = R.id.action_mainFragment_to_creatorFragment
                    findNavController().navigate(
                        resId = action,
                        args = bundle,
                        navOptions = NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_anim)
                            .build()
                    )
                }
            }, requireActivity().applicationContext)

            mainScreenRecyclerView.layoutManager = GridLayoutManager(context, 1)
            mainScreenRecyclerView.adapter = adapter
            
            viewModel.fetchTasksFromDatabase()
            viewModel.tasksListData.observe(viewLifecycleOwner) { itemsList ->
                adapter.fillRecyclerWithFullItemsList(itemsList)
            }

        }

        return binding.root
    }

}
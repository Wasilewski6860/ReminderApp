package com.example.reminderapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.databinding.ReminderStatisticScreenBinding

private val TEST_ITEMS_LIST = listOf(
    RcItem("Drink water", "Every 10 min"),
    RcItem("Drink water", "Every 10 min"),
    RcItem("Drink water", "Every 10 min"),
    RcItem("Drink water", "Every 10 min"),
    RcItem("Drink water", "Every 10 min"),
    RcItem("Drink water", "Every 10 min"),
    RcItem("Drink water", "Every 10 min"),
    RcItem("Drink water", "Every 10 min"),
    RcItem("Drink water", "Every 10 min"),
    RcItem("Drink water", "Every 10 min")
)

class TempStatisticFragment(private val actContext: Context) : Fragment() {

    private lateinit var binding: ReminderStatisticScreenBinding
    private val adapter = RecyclerViewTempAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReminderStatisticScreenBinding.inflate(inflater, container, false)

        binding.apply {

            statisticRecyclerView.layoutManager = GridLayoutManager(actContext, 1)
            statisticRecyclerView.adapter = adapter

            for (item in TEST_ITEMS_LIST) {
                adapter.addReminderItem(item)
            }

        }

        return binding.root
    }

}
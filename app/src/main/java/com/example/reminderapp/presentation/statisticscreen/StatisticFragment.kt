package com.example.reminderapp.presentation.statisticscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.R
import com.example.reminderapp.databinding.StatisticFragmentBinding
import com.example.reminderapp.presentation.BackActionInterface
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticFragment : Fragment(), BackActionInterface {

    private lateinit var binding: StatisticFragmentBinding
    private val viewModel by viewModel<StatisticViewModel>()
    private val adapter = StatisticScreenRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StatisticFragmentBinding.inflate(inflater, container, false)

        binding.apply {

            statisticRecyclerView.layoutManager = GridLayoutManager(context, 1)
            statisticRecyclerView.adapter = adapter

            viewModel.fetchTasksFromDatabase()
            viewModel.tasksListLiveData.observe(viewLifecycleOwner) { tasksList ->
                adapter.fillRecyclerViewWithItems(tasksList)
            }

        }

        initListeners(findNavController())
        initChart()

        return binding.root
    }

    private fun initListeners(navController: NavController) = with(binding) {
        requireActivity().findViewById<FloatingActionButton>(R.id.floatingButton)
            .setOnClickListener {
                goBack(navController)
            }
    }

    override fun goBack(navController: NavController) {
        navController.navigate(
            resId = R.id.mainFragment,
            args = null,
            navOptions = NavOptions.Builder().setExitAnim(R.anim.slide_out_anim).build()
        )
    }

    private fun initChart() = with(binding) {
        val itemsList = listOf(
            Item(name = "First task", counter = 3),
            Item(name = "Second task", counter = 7),
            Item(name = "Third task", counter = 5)
        )

        val barEntries = ArrayList<BarEntry>()

        itemsList.forEachIndexed { index, item ->
            val entry = BarEntry(index.toFloat(), item.counter.toFloat())
            barEntries.add(entry)
        }

        val dataSet = BarDataSet(barEntries, "Items").apply {
            axisDependency = YAxis.AxisDependency.LEFT
        }
        val barData = BarData(dataSet)

        statisticChartView.apply {
            data = barData
            setFitBars(true)
            animateY(1300)

//            legend.apply {
//                isEnabled = true
//                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
//                verticalAlignment = Legend.LegendVerticalAlignment.TOP
//                form = Legend.LegendForm.DEFAULT
//                textSize = 50f
//                textColor = Color.BLACK
//            }

            xAxis.apply {
                isGranularityEnabled = true
                granularity = 1f
                setLabelCount(itemsList.size, true)
            }

            axisRight.setDrawLabels(false)

            invalidate()
        }
    }

}

// temp data class for chart testing
private data class Item(
    val name: String,
    val counter: Int
)
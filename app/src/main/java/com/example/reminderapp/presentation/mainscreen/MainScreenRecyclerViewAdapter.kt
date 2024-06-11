package com.example.reminderapp.presentation.mainscreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Task
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ListItemRecyclerBinding

class MainScreenRecyclerViewAdapter(
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<MainScreenRecyclerViewAdapter.ItemHolder>() {

    private val itemsList = mutableListOf<Task>() /** change this on TaskGroup model item later */
    // private val timesDict = SpinnerPeriodicTime.getTimesDict(context)

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemRecyclerBinding.bind(view)

        fun bind(item: Task) = with(binding) {
            /**
             * Some test code strings
            // listNameTextView.text = item.groupName
            // listItemsCounterTextView.text = item.itemsInGroupCounter
            */

            // reminderRcItemTime.text = changeTimeFormat(item) ?: "ERROR" <- can use something like this later
            mainRecyclerViewItemHolder.setOnClickListener {
                listener.onRcItemClick(position = adapterPosition)
            }
        }

        /**
         * Need this in another adapter
//        private fun changeTimeFormat(task: Task): String? {
//            when (task.type) {
//                TaskPeriodType.ONE_TIME -> {
//                    val formatDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
//                    val date = Date(task.reminderCreationTime + task.reminderTimeTarget)
//                    return formatDate.format(date)
//                }
//                TaskPeriodType.PERIODIC -> {
//                    return timesDict[task.reminderTimeTarget]
//                }
//            }
//        }
         */

    }

    interface OnItemClickListener {
        fun onRcItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_recycler, parent, false
        )
        return ItemHolder(view)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(itemsList[position])
    }

    fun getItemByPosition(position: Int): Task {
        return itemsList[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: Task) {
        itemsList.add(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillRecyclerWithFullItemsList(itemsMutableList: List<Task>) {
        for (item in itemsMutableList) {
            itemsList.add(item)
            notifyDataSetChanged()
        }
    }

    fun getItemPosition(item: Task): Int = itemsList.indexOf(item)

    fun changeItem(position: Int, item: Task) {
        itemsList[position] = item
        notifyItemChanged(position)
    }

}
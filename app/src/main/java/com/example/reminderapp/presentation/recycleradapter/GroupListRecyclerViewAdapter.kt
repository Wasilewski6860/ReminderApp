package com.example.reminderapp.presentation.recycleradapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Group
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ListItemRecyclerBinding

class GroupListRecyclerViewAdapter(
    private val listener: OnItemClickListener,
    private val isDeleteIconVisible: Boolean = false
) : RecyclerView.Adapter<GroupListRecyclerViewAdapter.ItemHolder>() {

    private val groupsList = mutableListOf<Group>()

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemRecyclerBinding.bind(view)

        fun bind(item: Group) = with(binding) {
            listNameTextView.text = item.groupName
            colorCircleItem.circleColor = item.groupColor
            /**
             *  Use there image setting like this:
             * colorCircleItem.bitmap = item.image
             * **/

            mainRecyclerViewItemHolder.setOnClickListener {
                listener.onRcItemClick(position = adapterPosition)
            }

            trashImageView.apply {
                when (isDeleteIconVisible) {
                    false -> this.visibility = View.GONE
                    else -> this.visibility = View.VISIBLE
                }
                setOnClickListener { listener.onDeleteIconClick(position = adapterPosition) }
            }
        }
    }

    interface OnItemClickListener {

        fun onRcItemClick(position: Int)

        fun onDeleteIconClick(position: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_recycler, parent, false
        )
        return ItemHolder(view)
    }

    override fun getItemCount(): Int = groupsList.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(groupsList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillRecyclerWithFullItemsList(itemsList: List<Group>) {
        itemsList.forEach {
            groupsList.add(it)
            notifyDataSetChanged()
        }
    }

    fun deleteItem(position: Int) {
        groupsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun collectInfo(position: Int): Group {
        return groupsList[position]
    }

}
package com.example.reminderapp.presentation.recycleradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Group
import com.example.reminderapp.animations.playRecyclerItemDeletingAnimation
import com.example.reminderapp.databinding.ListItemRecyclerBinding
import org.koin.core.component.KoinComponent

class GroupListRecyclerViewAdapter(
    private val listener: OnItemClickListener,
    private val isDeleteIconVisible: Boolean = false
) : ListAdapter<Group, GroupListRecyclerViewAdapter.GroupViewHolder>(DiffCallBack), KoinComponent {

    class GroupViewHolder(val binding: ListItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemRecyclerBinding.inflate(inflater, parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            listNameTextView.text = item.groupName

//            colorCircleItem.circleColor = item.groupColor
//            item.groupImage?.let { colorCircleItem.bitmap = it }

            listItemsCounterTextView.text = item.tasksCount.toString()

            mainRecyclerViewItemHolder.setOnClickListener {
                listener.onRcItemClick(item)
            }

            trashImageView.apply {
                when (isDeleteIconVisible) {
                    false -> this.visibility = android.view.View.GONE
                    else -> this.visibility = android.view.View.VISIBLE
                }
                setOnClickListener {
                    playRecyclerItemDeletingAnimation(mainRecyclerViewItemHolder) {
                        listener.onDeleteIconClick(item)
                    }
                }
            }
        }

    }

    fun deleteItem(itemToDelete: Group) {
        val items = currentList.toMutableList()
        val position = items.indexOfFirst { it == itemToDelete }

        if (position != RecyclerView.NO_POSITION) {
            val items = currentList.toMutableList()
            items.removeAt(position)
            submitList(items)
        }
    }

    interface OnItemClickListener {

        fun onRcItemClick(group: Group)

        fun onDeleteIconClick(group: Group)

    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Group>() {

            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem.groupId === newItem.groupId
            }

            override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem == newItem
            }
        }
    }

}

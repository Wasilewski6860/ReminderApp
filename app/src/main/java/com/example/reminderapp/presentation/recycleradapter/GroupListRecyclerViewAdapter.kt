package com.example.reminderapp.presentation.recycleradapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Group
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ListItemRecyclerBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class GroupListRecyclerViewAdapter(
    private val listener: OnItemClickListener,
    private val isAdapterForMainScreen: Boolean = true
) : RecyclerView.Adapter<GroupListRecyclerViewAdapter.ItemHolder>() {

    private val groupsList = mutableListOf<Group>()
    private val isDeleteIconVisibleFlowData = MutableStateFlow(false)
    val isDeleteIconVisible get() = isDeleteIconVisibleFlowData

    // private val deleteImage by lazy { ImageView(context) }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemRecyclerBinding.bind(view)

        fun bind(item: Group) = with(binding) {
            listNameTextView.text = item.groupName
            colorCircleItem.circleColor = item.groupColor

            mainRecyclerViewItemHolder.setOnClickListener {
                listener.onRcItemClick(position = adapterPosition)
            }

            trashImageView.apply {
                when (isAdapterForMainScreen) {
                    true -> this.visibility = View.GONE
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

//    fun deleteIconVisibility(visible: Boolean) {
//        when (visible) {
//            true -> deleteImage.visibility = View.VISIBLE
//            else -> deleteImage.visibility = View.GONE
//        }
//    }

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

}
package com.example.reminderapp.presentation.create_reminder.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.domain.model.Group
import com.example.reminderapp.R

class GroupSpinnerAdapter(
    context: Context,
    private val onNoneSelected: () -> Unit,
    private val onCreateGroup: () -> Unit,
    private val onGroupSelected: (Group) -> Unit
) : ArrayAdapter<String>(
    context,
    android.R.layout.simple_spinner_item,
    listOf(context.getString(R.string.not_selected))  + context.getString(R.string.create)
) {

    private var groupItems = mutableListOf<Group>()

    fun submitList(groups: List<Group>) {
        groupItems.clear()
        groupItems.addAll(groups)
        val list = mutableListOf<String>()
        list.add(context.getString(R.string.not_selected))
        list.addAll(groupItems.map { it.groupName })
        list.add(context.getString(R.string.create))
        notifyDataSetChanged()
    }


    override fun getCount(): Int {
        // +2 для "Не выбрано" и "Создать список"
        return groupItems.size + 2
    }

    override fun getItem(position: Int): String? {
        return when (position) {
            0 -> context.getString(R.string.not_selected)
            count - 1 -> context.getString(R.string.create)
            else -> groupItems[position - 1].groupName
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)
        val padding = context.resources.getDimensionPixelSize(R.dimen.tiny_margin)
        textView.setPadding(padding, padding, padding, padding)
        return view
    }

    fun handleItemSelected(position: Int) {
        when (position) {
            0 -> onNoneSelected()
            count - 1 -> onCreateGroup()
            else -> onGroupSelected(groupItems[position - 1])
        }
    }

    fun getGroupPosition(groupId: Int): Int {
        val index = groupItems.indexOfFirst { it.groupId == groupId }
        return if (index >= 0) index + 1 else -1 // +1 чтобы учесть "Не выбрано"
    }

    fun getGroupPosition(groupName: String?): Int {
        val index = groupItems.indexOfFirst { it.groupName == groupName }
        return if (index >= 0) index + 1 else 0 // +1 чтобы учесть "Не выбрано"
    }
}

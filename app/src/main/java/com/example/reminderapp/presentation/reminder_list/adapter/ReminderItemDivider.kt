package com.example.reminderapp.presentation.reminder_list.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.R

class ReminderItemDivider(context: Context) : RecyclerView.ItemDecoration() {
    private val paint: Paint = Paint().apply {
        color = context.getColor(android.R.color.darker_gray)
        strokeWidth = context.resources.getDimensionPixelSize(R.dimen.divider_height).toFloat()
    }

    private val verticalPadding: Int = context.resources.getDimensionPixelSize(R.dimen.divider_vertical_padding)
    private val horizontalPadding: Int = context.resources.getDimensionPixelSize(R.dimen.divider_horizontal_padding)
    private val itemTopPadding: Int = context.resources.getDimensionPixelSize(R.dimen.item_top_padding)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + horizontalPadding
        val right = parent.width - parent.paddingRight - horizontalPadding

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (parent.getChildAdapterPosition(child) == parent.adapter?.itemCount?.minus(1)) {
                // Не рисовать разделитель для последнего элемента
                continue
            }
            val params = child.layoutParams as RecyclerView.LayoutParams

            // Позиция для разделителя (линии)
            val top = child.bottom + params.bottomMargin + verticalPadding
            val bottom = top + paint.strokeWidth.toInt()

            // Рисуем линию
            c.drawLine(left.toFloat(), top.toFloat(), right.toFloat(), top.toFloat(), paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(horizontalPadding, itemTopPadding, horizontalPadding, verticalPadding)
    }
}
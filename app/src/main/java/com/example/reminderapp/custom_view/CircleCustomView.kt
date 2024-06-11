package com.example.reminderapp.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.reminderapp.R
import kotlin.math.min

class CircleCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var paint: Paint = Paint().apply {
        color = context.getColor(R.color.red)
        style = Paint.Style.FILL
    }

    var circleColor: Int
        get() = paint.color
        set(value) {
            paint.color = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, min(width, height) / 2f, paint)
    }

}
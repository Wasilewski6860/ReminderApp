package com.example.reminderapp.custom_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    private var _bitmap: Bitmap

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

    init {
        _bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.clock_icon)
    }

    private fun loadBitmapFromResource(context: Context, resourceId: Int) {
        _bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
    }

    var bitmap: Int
        get() = 0
        set(value) {
            _bitmap = BitmapFactory.decodeResource(context.resources, value)
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(width / 2f, height / 2f, min(width, height) / 2f, paint)

        canvas.drawBitmap(
            _bitmap,
            width / 2f - _bitmap.width / 2f,
            height / 2f - _bitmap.height / 2f,
            null
        )
    }

}
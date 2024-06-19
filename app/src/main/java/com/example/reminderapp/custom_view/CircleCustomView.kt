package com.example.reminderapp.custom_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.min

class CircleCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var _bitmap: Bitmap? = null
    var _drawable: Drawable? = null
    private var _isVisible = true

    private var paint: Paint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.FILL
    }

    private var allocation: Paint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.FILL
    }

    var circleColor: Int
        get() = paint.color
        set(value) {
            paint.color = value
            invalidate()
        }

    var circleAllocation: Boolean
        get() = allocation.color != Color.TRANSPARENT
        set(value) {
            when (value) {
                true -> allocation.color = Color.BLACK // Test color variant
                false -> allocation.color = Color.TRANSPARENT
            }
            invalidate()
        }

    /**
     * Use that value to set image drawable to this view element
     * Vector asset size need be 25dp x 25dp or less to display the image correctly
     * Usage example:
     * val circleView = findViewById<CircleCustomView>(R.id.my_circle_view)
     * circleView.bitmap = R.drawable.my_vector_asset
     * **/
    var bitmap: Int?
        get() = null
        set(value) {
            if (value != null) {
                _drawable = ContextCompat.getDrawable(context, value)
                _drawable?.let {
                    val bp = Bitmap.createBitmap(
                        it.intrinsicWidth,
                        it.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                        )
                    val canvas = Canvas(bp)
                    it.setBounds(0, 0, canvas.width, canvas.height)
                    it.draw(canvas)
                    _bitmap = bp
                }
            }
            invalidate()
        }

    var visible: Boolean
        get() = _isVisible
        set(value) {
            _isVisible = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /** Use it to allocate circle (test variant) **/
        // canvas.drawCircle(width / 2f, height / 2f, min(width, height) / 2f, allocation)
        // canvas.drawCircle(width / 2f, height / 2f, min(width, height) / 2f - 5f, paint)
        canvas.drawCircle(width / 2f, height / 2f, min(width, height) / 2f, paint)

        if (_bitmap != null && _isVisible) {
            canvas.drawBitmap(
                _bitmap!!,
                width / 2f - _bitmap!!.width / 2f,
                height / 2f - _bitmap!!.height / 2f,
                null
            )
        }
    }

}
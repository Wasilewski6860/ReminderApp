package com.example.reminderapp.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.reminderapp.R

@SuppressLint("UseCompatLoadingForDrawables")
class GridItemCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var imageView: ImageView
    private var counterTextView: TextView
    private var titleTextView: TextView

    init {
        inflate(context, R.layout.grid_item, this)
        imageView = findViewById(R.id.imageGridItem)
        counterTextView = findViewById(R.id.textCounterGridItem)
        titleTextView = findViewById(R.id.nameTextGridItem)

        attrs?.let {
            val attrArray = context.theme.obtainStyledAttributes(
                attrs, R.styleable.GridItemCustomView, 0, 0
            )
            val image = attrArray.getResourceId(
                R.styleable.GridItemCustomView_gridImage,
                R.drawable.clock_icon
            )
            val counterText = attrArray.getResourceId(
                R.styleable.GridItemCustomView_gridCounterText,
                R.string.stub
            )
            val titleText = attrArray.getResourceId(
                R.styleable.GridItemCustomView_gridTitleText,
                R.string.stub
            )
            attrArray.recycle()

            imageView.setImageDrawable(context.getDrawable(image))
            counterTextView.text = context.getString(counterText)
            titleTextView.text = context.getString(titleText)
        }
    }

}
package com.example.reminderapp.utils

import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class AnimationUtils {

    companion object {
        fun expandLayoutToBottom(duration: Int = 500,baseContainer: ConstraintLayout, neededToExpandContainer: LinearLayout) {
            val layoutParams = baseContainer.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.height = 0
            neededToExpandContainer.layoutParams = layoutParams

            val animator = ValueAnimator.ofInt(0, getContentHeight(neededToExpandContainer)).apply {
                addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    layoutParams.height = value
                    neededToExpandContainer.layoutParams = layoutParams
                    val y: Float = baseContainer.height.toFloat() - baseContainer.height.toFloat()
                    neededToExpandContainer.y = y
                }
            }
            animator.start()
        }

        fun collapseLayoutToTop(duration: Int = 500, baseContainer: ConstraintLayout, neededToCollapseContainer: LinearLayout) {
            val layoutParams = neededToCollapseContainer.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.height = getContentHeight(neededToCollapseContainer)
            neededToCollapseContainer.layoutParams = layoutParams

            val animator = ValueAnimator.ofInt(getContentHeight(neededToCollapseContainer), 0).apply {
                addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    layoutParams.height = value
                    neededToCollapseContainer.layoutParams = layoutParams
                    val y: Float = baseContainer.height.toFloat() - neededToCollapseContainer.height.toFloat()
                    neededToCollapseContainer.y = y
                }
            }
            animator.start()
        }

        private fun getContentHeight(view: ViewGroup): Int {
            var height = 0
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                child.measure(0, 0)
                height += child.measuredHeight
            }
            return height
        }

        private fun getContentWidth(view: ViewGroup): Int {
            var width = 0
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                child.measure(0, 0)
                width += child.measuredWidth
            }
            return width
        }
    }



}
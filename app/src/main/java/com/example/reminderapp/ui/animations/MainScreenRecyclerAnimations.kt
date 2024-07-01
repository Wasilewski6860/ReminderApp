package com.example.reminderapp.ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun hideRecyclerAnimation(recyclerView: RecyclerView) {
    val alphaAnimation = ObjectAnimator.ofFloat(
        recyclerView, "alpha", 1f, 0f
    )
    val translateYAnimation = ObjectAnimator.ofFloat(
        recyclerView, "translationY", 0f, -recyclerView.width.toFloat()
    )

    val animatorSet = AnimatorSet()
    animatorSet.playTogether(alphaAnimation, translateYAnimation)

    animatorSet.duration = 500

    animatorSet.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            recyclerView.visibility = View.GONE
        }
    })

    animatorSet.start()
}

fun showRecyclerAnimation(recyclerView: RecyclerView) {
    val alphaAnimation = ObjectAnimator.ofFloat(
        recyclerView, "alpha", 0f, 1f
    )
    val translateYAnimation = ObjectAnimator.ofFloat(
        recyclerView, "translationY", -recyclerView.height.toFloat(), 0f
    )

    val animatorSet = AnimatorSet()
    animatorSet.playTogether(alphaAnimation, translateYAnimation)

    animatorSet.duration = 500

    animatorSet.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
            recyclerView.visibility = View.VISIBLE
        }
    })

    animatorSet.start()
}
package com.example.reminderapp.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

fun playRecyclerItemDeletingAnimation(view: View, action: () -> Unit) {
    val alphaAnimation = ObjectAnimator.ofFloat(
        view, "alpha", 1f, 0f
    )
    val translateXAnimation = ObjectAnimator.ofFloat(
        view, "translationX", 0f, -view.width.toFloat()
    )

    val animatorSet = AnimatorSet()
    animatorSet.playTogether(alphaAnimation, translateXAnimation)

    animatorSet.duration = 400

    animatorSet.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            action()
        }
    })

    animatorSet.start()
}
package com.example.reminderapp.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun playFloatingButtonAnimation(floatingButton: FloatingActionButton) {
    val scaleXAnimator = ObjectAnimator.ofFloat(floatingButton, "scaleX", 1f, 1.2f)
    val scaleYAnimator = ObjectAnimator.ofFloat(floatingButton, "scaleY", 1f, 1.2f)

    val testScaleX = ObjectAnimator.ofFloat(floatingButton, "scaleX", 1.2f, 1f)
    val testScaleY = ObjectAnimator.ofFloat(floatingButton, "scaleY", 1.2f, 1f)

    val combinedAnimator = AnimatorSet()
    combinedAnimator.playTogether(scaleXAnimator, scaleYAnimator)
    combinedAnimator.playTogether(testScaleX, testScaleY)

    combinedAnimator.duration = 1000
    combinedAnimator.interpolator = DecelerateInterpolator()
    combinedAnimator.start()
}
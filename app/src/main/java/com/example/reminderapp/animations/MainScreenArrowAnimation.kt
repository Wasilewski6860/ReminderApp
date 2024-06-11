package com.example.reminderapp.animations

import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView

fun animateImageViewRotation(imageView: ImageView, currentArrowRotation: Float) {
    val rotationAnimation = ObjectAnimator.ofFloat(
        imageView,
        "rotation",
        imageView.rotation,
        currentArrowRotation
    )
    rotationAnimation.duration = 500
    rotationAnimation.interpolator = DecelerateInterpolator()
    rotationAnimation.start()
}
package com.example.shikimoriandroid.ui.utils

import android.animation.ValueAnimator
import android.content.res.Resources
import android.transition.TransitionManager
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams

fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)

fun View.expand(root: ViewGroup) {
    TransitionManager.beginDelayedTransition(root)
    this.updateLayoutParams {
        height = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}

fun View.collapse(toHeight: Int, root: ViewGroup) {
    TransitionManager.beginDelayedTransition(root)
    this.updateLayoutParams {
        height = toHeight
    }
}

//fun View.expand(from: Int) {
//    ValueAnimator.ofInt(70, 150).apply {
//        duration = 1000L
//        addUpdateListener {
//            Log.i("TAG", "animatedValue = $animatedValue")
//            this@expand.updateLayoutParams { height = (animatedValue as Int) }
//        }
//        start()
//    }
//}
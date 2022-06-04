package com.example.shikimoriandroid.ui.utils

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.transition.TransitionManager
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.anime.Screenshot
import com.example.shikimoriandroid.data.model.anime.Stats
import com.example.shikimoriandroid.databinding.AnimePageGistItemBinding
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.stfalcon.imageviewer.StfalconImageViewer

fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)

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

fun List<String>.roleFilter(roles: List<String>): Boolean {
    this.forEach { thisRole ->
        if (roles.contains(thisRole)) {
            return true
        }
    }
    return false
}

fun ChipGroup.addChip(context: Context, label: String, onClick: () -> Unit) {
    Chip(context).apply {
        id = View.generateViewId()
        text = label
        isClickable = true
        isCheckable = true
        isCheckedIconVisible = false
        isFocusable = true
        setOnClickListener { onClick() }
        addView(this)
    }
}

fun ViewGroup.addViewWithLayout(stats: Stats, context: Context, maxValue: Int, maxWidth: Int) {
    var valueIsVisible = true
    val widthPercent = (100 * stats.value) / maxValue
    if (widthPercent <= 10) {
        valueIsVisible = false
    }

    val li = LayoutInflater.from(context)
    val layout: View = li.inflate(R.layout.anime_page_gist_item, null)
    val lBinding = AnimePageGistItemBinding.bind(layout)
    lBinding.name.text = stats.name

    if (stats.value == maxValue) {
        lBinding.value.expand(maxWidth) {
            lBinding.value.text = stats.value.toString()
        }
    } else {
        val newWidth = (maxWidth * stats.value) / maxValue
        lBinding.value.expand(newWidth) {
            if (valueIsVisible) {
                lBinding.value.text = stats.value.toString()
            }

        }
    }

    this.addView(layout)
}

fun View.expand(to: Int, onEnd: () -> Unit) {
    ValueAnimator.ofInt(0, to).apply {
        duration = 1000L
        addUpdateListener {
            this@expand.updateLayoutParams { width = (animatedValue as Int) }
        }
        this.doOnEnd { onEnd() }
        start()
    }
}

fun ImageView.openImageViewer(fragment: Fragment, screenshots: List<Screenshot>, position: Int) {
    val glide = GlideAdapter(fragment)
    StfalconImageViewer.Builder(
        fragment.requireContext(),
        screenshots
    ) { imageView, screenshot ->
        glide.loadImage(
            "https://shikimori.one/${screenshot.original}",
            imageView
        )
    }.withStartPosition(position)
        .withTransitionFrom(this)
        .show()
}
package com.example.shikimoriandroid.ui.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.transition.TransitionManager
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.anime.ExternalLink
import com.example.shikimoriandroid.data.model.anime.Screenshot
import com.example.shikimoriandroid.data.model.anime.Stats
import com.example.shikimoriandroid.data.model.user.ActivityItem
import com.example.shikimoriandroid.databinding.AnimePageGistItemBinding
import com.example.shikimoriandroid.databinding.ExternalLinkItemBinding
import com.example.shikimoriandroid.databinding.UserActivityItemBinding
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.stfalcon.imageviewer.StfalconImageViewer
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)

fun View.expandWidth(root: ViewGroup) {
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

fun ViewGroup.addViewWithLayout(stats: Stats, maxValue: Int, maxWidth: Int) {
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
        lBinding.value.expandWidth(maxWidth) {
            lBinding.value.text = stats.value.toString()
        }
    } else {
        val newWidth = (maxWidth * stats.value) / maxValue
        lBinding.value.expandWidth(newWidth) {
            if (valueIsVisible) {
                lBinding.value.text = stats.value.toString()
            }

        }
    }

    this.addView(layout)
}

fun ViewGroup.addViewOnUserActivity(activityItem: ActivityItem, maxValue: Int, maxHeight: Int) {
    var valueIsVisible = true
    val heightPercent = (100 * activityItem.value) / maxValue
    if (heightPercent <= 1) {
        valueIsVisible = false
    }

    val li = LayoutInflater.from(context)
    val layout: View = li.inflate(R.layout.user_activity_item, null)
    val lBinding = UserActivityItemBinding.bind(layout)

    lBinding.value.setOnClickListener {
        val firstDate = activityItem.date[0].toDateTime()
        val secondDate = activityItem.date[1].toDateTime()
        val message = "${activityItem.value}: с $firstDate по $secondDate"
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    if (activityItem.value == maxValue) {
        lBinding.value.expandHeight(maxHeight) {
            lBinding.value.text = activityItem.value.toString()
        }
    } else {
        val newHeight = (maxHeight * activityItem.value) / maxValue
        lBinding.value.expandHeight(newHeight) {
            if (valueIsVisible) {
                lBinding.value.text = activityItem.value.toString()
            }
        }

    }

    this.addView(layout)
}

fun ViewGroup.addLink(externalLink: ExternalLink, onClick: () -> Unit) {
    val li = LayoutInflater.from(context)
    val layout: View = li.inflate(R.layout.external_link_item, null)
    val lBinding = ExternalLinkItemBinding.bind(layout)

    lBinding.link.text = externalLink.name.replace('_', ' ')
    lBinding.link.setOnClickListener { onClick() }

    this.addView(layout)
}

fun View.expandWidth(to: Int, onEnd: () -> Unit) {
    ValueAnimator.ofInt(0, to).apply {
        duration = 1000L
        addUpdateListener {
            this@expandWidth.updateLayoutParams { width = (animatedValue as Int) }
        }
        this.doOnEnd { onEnd() }
        start()
    }
}

fun View.expandHeight(to: Int, onEnd: () -> Unit) {
    ValueAnimator.ofInt(0, to).apply {
        duration = 1000L
        addUpdateListener {
            this@expandHeight.updateLayoutParams { height = (animatedValue as Int) }
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

fun View.shakeAnimation() {
    val left = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 10F).apply {
        duration = 100L
    }

    val right = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 10F, -20F).apply {
        duration = 100L
    }

    val default = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, -20F, 0F).apply {
        duration = 100L
    }

    val animatorSet = AnimatorSet()
    animatorSet.play(right).before(default).after(left)
    animatorSet.start()
}

fun View.hide() {
    this.isVisible = false
}

fun View.show() {
    this.isVisible = true
}

fun Fragment.toastLong(text: String) {
    Toast.makeText(this.requireContext(), text, Toast.LENGTH_LONG).show()
}

fun Fragment.toastShort(text: String) {
    Toast.makeText(this.requireContext(), text, Toast.LENGTH_SHORT).show()
}

private fun Long.toDateTime(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    val netDate = Date(this * 1000)
    return sdf.format(netDate)
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseDate(date: String): String {
    // пример даты: 2022-06-03T22:42:36.314+03:00
    val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ"
    val dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern))
    val dmy = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    val hm = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    return "$dmy в $hm"
}
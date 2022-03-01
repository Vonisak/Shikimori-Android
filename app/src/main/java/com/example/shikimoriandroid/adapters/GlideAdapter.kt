package com.example.shikimoriandroid.adapters

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

class GlideAdapter(val fragment: Fragment) {
    fun loadImage(url: String, imageView: ImageView) {
        val circularProgressDrawable = fragment.context?.let { CircularProgressDrawable(it) }
        circularProgressDrawable?.strokeWidth = 5f
        circularProgressDrawable?.centerRadius = 30f
        circularProgressDrawable?.start()
        Glide
            .with(fragment)
            .load(url)
            .placeholder(circularProgressDrawable)
            .into(imageView)
    }
}
package com.example.shikimoriandroid.ui.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.data.model.anime.Screenshot
import com.example.shikimoriandroid.databinding.ScreenshotItemBinding
import com.example.shikimoriandroid.ui.adapters.GlideAdapter

class ScreenshotItemViewHolder(val view: View, private val glideAdapter: GlideAdapter) :
    RecyclerView.ViewHolder(view) {
    val binding = ScreenshotItemBinding.bind(view)

    fun bind(screenshot: Screenshot, position: Int, onScreenshotClick: (imageView: ImageView, position: Int) -> Unit) {
        glideAdapter.loadImage("https://shikimori.one/${screenshot.preview}", binding.screenshot)
        binding.screenshot.setOnClickListener {
            onScreenshotClick(binding.screenshot, position)
        }
    }
}
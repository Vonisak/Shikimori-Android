package com.example.shikimoriandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.anime.Screenshot
import com.example.shikimoriandroid.ui.viewholder.ScreenshotItemViewHolder

class ScreenshotsAdapter(
    private val glide: GlideAdapter,
    private val onScreenshotClick: (imageView: ImageView, position: Int) -> Unit
) : RecyclerView.Adapter<ScreenshotItemViewHolder>() {

    var screenshots: List<Screenshot> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.screenshot_item, parent, false)
        return ScreenshotItemViewHolder(view, glide)
    }

    override fun onBindViewHolder(holder: ScreenshotItemViewHolder, position: Int) {
        holder.bind(screenshots[position], position, onScreenshotClick)
    }

    override fun getItemCount(): Int = screenshots.size
}
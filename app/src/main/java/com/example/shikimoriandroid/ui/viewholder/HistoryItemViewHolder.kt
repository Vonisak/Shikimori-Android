package com.example.shikimoriandroid.ui.viewholder

import android.os.Build
import android.text.Html
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.data.model.user.History
import com.example.shikimoriandroid.databinding.HistoryItemBinding
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.ui.utils.parseDate

class HistoryItemViewHolder(val view: View, private val glideAdapter: GlideAdapter) :
    RecyclerView.ViewHolder(view) {
    val binding = HistoryItemBinding.bind(view)

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(history: History, onHistoryClick: (animeId: Int) -> Unit) = with(binding) {
        if (history.animeInfo != null) {
            glideAdapter.loadImage(
                "https://shikimori.one/${history.animeInfo.poster.preview}",
                poster
            )
            targetName.text = history.animeInfo.nameRus
            description.text = Html.fromHtml(history.description, Html.FROM_HTML_MODE_COMPACT)
            date.text = parseDate(history.date)
            poster.setOnClickListener {
                onHistoryClick(history.animeInfo.id)
            }
            targetName.setOnClickListener {
                onHistoryClick(history.animeInfo.id)
            }
        }
    }
}
package com.example.shikimoriandroid.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.databinding.AnimeItemBinding
import com.example.shikimoriandroid.ui.adapters.GlideAdapter

class AnimeItemViewHolder(val view: View, private val glideAdapter: GlideAdapter) :
    RecyclerView.ViewHolder(view) {
    val binding = AnimeItemBinding.bind(view)

    fun bind(anime: AnimeInfo, onAnimeClick: (animeId: Int) -> Unit) = with(binding) {
        glideAdapter.loadImage("https://shikimori.one/${anime.poster.original}", animeItemPoster)
        animeItemName.text = anime.nameRus
        score.text = anime.score
        view.setOnClickListener {
            onAnimeClick(anime.id)
        }
    }
}
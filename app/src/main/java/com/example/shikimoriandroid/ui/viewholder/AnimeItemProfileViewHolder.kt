package com.example.shikimoriandroid.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.databinding.UserListItemBinding
import com.example.shikimoriandroid.domain.utils.AnimeStringSwitcher

class AnimeItemProfileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val binding = UserListItemBinding.bind(view)
    private val switcher = AnimeStringSwitcher()

    fun bind(animeRates: AnimeRates, onAnimeClick: (animeId: Int) -> Unit) = with(binding) {
        val score = if (animeRates.score == 0) "-" else animeRates.score.toString()

        animeName.text = animeRates.anime.nameRus
        animeScore.text = score
        animeEpisodes.text = animeRates.episodes.toString()
        animeTotalEpisodes.text = animeRates.anime.episodes
        animeType.text = switcher.kindSwitch(animeRates.anime.kind)

        animeName.setOnClickListener {
            onAnimeClick(animeRates.anime.id)
        }
    }
}
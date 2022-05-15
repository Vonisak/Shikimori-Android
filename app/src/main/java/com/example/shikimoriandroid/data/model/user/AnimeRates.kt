package com.example.shikimoriandroid.data.model.user

import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.google.gson.annotations.SerializedName

data class AnimeRates(
    @SerializedName("score")
    val score: Int,
    @SerializedName("episodes")
    val episodes: Int,
    @SerializedName("anime")
    val anime: AnimeInfo
)

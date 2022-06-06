package com.example.shikimoriandroid.data.model.user

import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName("created_at") val date: String,
    @SerializedName("target") val animeInfo: AnimeInfo?,
    @SerializedName("description") val description: String
)
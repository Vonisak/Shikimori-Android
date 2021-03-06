package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class AnimeGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("russian")
    val nameRus: String,
    @SerializedName("kind")
    val kind: String
)

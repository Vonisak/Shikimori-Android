package com.example.shikimoriandroid.model

import com.google.gson.annotations.SerializedName

data class AnimeImage(
    @SerializedName("preview")
    val preview: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("x96")
    val x96: String,
    @SerializedName("x48")
    val x48: String
)
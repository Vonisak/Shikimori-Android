package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("name") val name: String,
    @SerializedName("value") val value: Int
)
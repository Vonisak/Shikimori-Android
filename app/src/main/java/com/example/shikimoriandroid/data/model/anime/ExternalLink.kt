package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class ExternalLink(
    @SerializedName("kind") val name: String,
    @SerializedName("url") val url: String
)
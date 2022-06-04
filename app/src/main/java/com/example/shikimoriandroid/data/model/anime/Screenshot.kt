package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class Screenshot(
    @SerializedName("original") val original: String,
    @SerializedName("preview") val preview: String
)
package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class PersonPreview(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val nameEng: String,
    @SerializedName("russian") val nameRus: String,
    @SerializedName("image") val image: AnimeImage,
    @SerializedName("altname") val alterNames: String,
    @SerializedName("Japanese") val nameJap: String
)
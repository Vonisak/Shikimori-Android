package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class CharacterInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val nameEng: String,
    @SerializedName("russian") val nameRus: String,
    @SerializedName("image") val image: AnimeImage,
    @SerializedName("altname") val alterName: String,
    @SerializedName("japanese") val nameJap: String,
    @SerializedName("description") val description: String,
    @SerializedName("description_html") val descriptionHtml: String
)
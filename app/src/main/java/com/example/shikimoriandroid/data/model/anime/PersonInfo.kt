package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class PersonInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val nameEng: String,
    @SerializedName("russian") val nameRus: String,
    @SerializedName("image") val image: AnimeImage,
    @SerializedName("japanese") val nameJap: String,
    @SerializedName("job_title") val jobTitle: String,
    @SerializedName("birthday") val birthDate: String,
    @SerializedName("groupped_roles") val roles: List<List<String>>
)
package com.example.shikimoriandroid.data.model.user

import com.google.gson.annotations.SerializedName

data class UserAnimeStatuses(
    @SerializedName("id")
    val id: Int,
    @SerializedName("grouped_id")
    val groupedId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("size")
    val size: Int
)
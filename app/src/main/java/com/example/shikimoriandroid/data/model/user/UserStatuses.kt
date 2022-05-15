package com.example.shikimoriandroid.data.model.user

import com.google.gson.annotations.SerializedName

data class UserStatuses(
    @SerializedName("anime")
    val anime: List<UserAnimeStatuses>
)

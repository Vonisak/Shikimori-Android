package com.example.shikimoriandroid.data.model.user

import com.google.gson.annotations.SerializedName

data class UserStats(
    @SerializedName("statuses")
    val statuses: UserStatuses
)

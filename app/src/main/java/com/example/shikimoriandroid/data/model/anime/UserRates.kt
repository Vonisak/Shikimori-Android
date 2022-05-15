package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class UserRates(
    @SerializedName("user_rate")
    val userRate: UserRate
)

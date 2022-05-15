package com.example.shikimoriandroid.data.model.user

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("image")
    val images: ProfileImage,
    @SerializedName("stats")
    val stats: UserStats,
    @SerializedName("common_info")
    val commonInfo: List<String>
)

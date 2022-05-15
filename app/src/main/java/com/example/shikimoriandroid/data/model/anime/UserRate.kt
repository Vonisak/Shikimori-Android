package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class UserRate(
    @SerializedName("id")
    val id: Long,
    @SerializedName("score")
    val score: Int,
    @SerializedName("episodes")
    val episodes: Int?,
    @SerializedName("status")
    val status: String,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("target_id")
    val targetId: Int?,
    @SerializedName("target_type")
    val targetType: String?
)

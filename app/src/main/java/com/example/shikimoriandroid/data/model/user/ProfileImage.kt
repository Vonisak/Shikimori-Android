package com.example.shikimoriandroid.data.model.user

import com.google.gson.annotations.SerializedName

data class ProfileImage(
    @SerializedName("x160")
    val imageX160: String,
    @SerializedName("x148")
    val imageX148: String,
    @SerializedName("x80")
    val imageX80: String,
    @SerializedName("x64")
    val imageX64: String,
    @SerializedName("x48")
    val imageX48: String,
    @SerializedName("x32")
    val imageX32: String,
    @SerializedName("x16")
    val imageX16: String
)

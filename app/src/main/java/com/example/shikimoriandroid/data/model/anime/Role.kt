package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class Role(
    @SerializedName("roles") val rolesEng: List<String>,
    @SerializedName("roles_russian") val rolesRus: List<String>,
    @SerializedName("character") val characterPreview: CharacterPreview?,
    @SerializedName("person") val personPreview: PersonPreview?
)
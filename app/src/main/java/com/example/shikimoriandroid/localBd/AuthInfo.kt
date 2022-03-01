package com.example.shikimoriandroid.localBd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authInfo")
data class AuthInfo(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "access_token") val accessToken: String?,
    @ColumnInfo(name = "refresh_token") val refreshToken: String?
)

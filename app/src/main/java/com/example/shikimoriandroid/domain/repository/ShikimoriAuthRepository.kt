package com.example.shikimoriandroid.domain.repository

import com.example.shikimoriandroid.data.model.AuthResponse
import com.example.shikimoriandroid.domain.utils.Constants

interface ShikimoriAuthRepository {

    suspend fun getTokens(
        authCode: String
    ): AuthResponse

    suspend fun updateTokens(
        refreshToken: String
    ): AuthResponse

}
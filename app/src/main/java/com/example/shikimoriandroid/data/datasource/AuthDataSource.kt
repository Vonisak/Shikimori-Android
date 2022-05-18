package com.example.shikimoriandroid.data.datasource

import com.example.shikimoriandroid.data.model.AuthResponse
import com.example.shikimoriandroid.data.network.ShikimoriAuthApi
import javax.inject.Inject

class AuthDataSource @Inject constructor(private val api: ShikimoriAuthApi) {

    suspend fun getTokens(authCode: String): AuthResponse = api.getTokens(authCode = authCode)

    suspend fun updateTokens(refreshToken: String): AuthResponse =
        api.updateTokens(refreshToken = refreshToken)
}
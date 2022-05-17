package com.example.shikimoriandroid.domain.repository

import com.example.shikimoriandroid.data.model.AuthResponse
import com.example.shikimoriandroid.domain.utils.Constants

interface ShikimoriAuthRepository {

    suspend fun getTokens(
        userAgent: String = Constants.appName,
        grantType: String = "authorization_code",
        clientId: String = Constants.clientId,
        clientSecret: String = Constants.clientSecret,
        authCode: String,
        redirectUri: String = "urn:ietf:wg:oauth:2.0:oob"
    ): AuthResponse

}
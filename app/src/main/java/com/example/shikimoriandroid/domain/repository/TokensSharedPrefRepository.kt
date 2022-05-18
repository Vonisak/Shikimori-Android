package com.example.shikimoriandroid.domain.repository

interface TokensSharedPrefRepository {

    fun saveAccessToken(accessToken: String)

    fun getAccessToken(): String?

    fun saveRefreshToken(refreshToken: String)

    fun getRefreshToken(): String?

    fun clearTokens()
}
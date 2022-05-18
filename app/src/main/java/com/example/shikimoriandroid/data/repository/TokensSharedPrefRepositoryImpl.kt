package com.example.shikimoriandroid.data.repository

import com.example.shikimoriandroid.data.datasource.TokensDataSource
import com.example.shikimoriandroid.domain.repository.TokensSharedPrefRepository
import javax.inject.Inject

class TokensSharedPrefRepositoryImpl @Inject constructor(private val dataSource: TokensDataSource) :
    TokensSharedPrefRepository {
    override fun saveAccessToken(accessToken: String) =
        dataSource.saveAccessToken(accessToken = accessToken)

    override fun getAccessToken(): String? = dataSource.getAccessToken()

    override fun saveRefreshToken(refreshToken: String) =
        dataSource.saveRefreshToken(refreshToken = refreshToken)

    override fun getRefreshToken(): String? = dataSource.getRefreshToken()

    override fun clearTokens() = dataSource.clearSharedPref()
}
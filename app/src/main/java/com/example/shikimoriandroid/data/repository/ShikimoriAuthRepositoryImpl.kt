package com.example.shikimoriandroid.data.repository

import com.example.shikimoriandroid.data.datasource.AuthDataSource
import com.example.shikimoriandroid.data.network.ShikimoriAuthApi
import com.example.shikimoriandroid.data.model.AuthResponse
import com.example.shikimoriandroid.domain.repository.ShikimoriAuthRepository
import javax.inject.Inject

class ShikimoriAuthRepositoryImpl @Inject constructor(private val dataSource: AuthDataSource) :
    ShikimoriAuthRepository {

    override suspend fun getTokens(authCode: String): AuthResponse =
        dataSource.getTokens(authCode = authCode)

    override suspend fun updateTokens(refreshToken: String): AuthResponse =
        dataSource.updateTokens(refreshToken = refreshToken)
}
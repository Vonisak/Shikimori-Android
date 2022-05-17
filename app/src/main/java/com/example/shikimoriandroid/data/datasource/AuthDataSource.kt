package com.example.shikimoriandroid.data.datasource

import com.example.shikimoriandroid.data.network.ShikimoriAuthApi
import javax.inject.Inject

class AuthDataSource @Inject constructor(private val api: ShikimoriAuthApi) {

    suspend fun getTokens(authCode: String) = api.getTokens(authCode = authCode)
}
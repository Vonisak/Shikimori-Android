package com.example.shikimoriandroid.domain.repository

import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.data.model.user.History
import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.domain.utils.Constants

interface ShikimoriUserRepository {

    suspend fun getCurrentUser(
        userAgent: String = Constants.appName,
        accessToken: String
    ): UserInfo

    suspend fun getUserById(
        userAgent: String = Constants.appName,
        accessToken: String,
        id: Int
    ): UserInfo

    suspend fun getUserAnimeList(
        userAgent: String = Constants.appName,
        accessToken: String,
        userId: Int,
        count: Int,
        page: Int,
        status: String
    ): List<AnimeRates>

    suspend fun createRate(
        userAgent: String = Constants.appName,
        accessToken: String,
        userRate: UserRates
    )

    suspend fun getHistory(accessToken: String, userId: Int, limit: Int, page: Int): List<History>
}
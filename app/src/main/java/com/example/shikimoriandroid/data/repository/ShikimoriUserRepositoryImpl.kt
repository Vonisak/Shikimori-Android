package com.example.shikimoriandroid.data.repository

import com.example.shikimoriandroid.data.datasource.UserDataSource
import com.example.shikimoriandroid.data.network.ShikimoriUserApi
import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.data.model.user.History
import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.domain.repository.ShikimoriUserRepository
import javax.inject.Inject

class ShikimoriUserRepositoryImpl @Inject constructor(private val dataSource: UserDataSource) :
    ShikimoriUserRepository {

    override suspend fun getCurrentUser(userAgent: String, accessToken: String): UserInfo =
        dataSource.getCurrentUser(accessToken = accessToken)

    override suspend fun getUserById(userAgent: String, accessToken: String, id: Int): UserInfo =
        dataSource.getUserById(accessToken = accessToken, id = id)

    override suspend fun getUserAnimeList(
        userAgent: String,
        accessToken: String,
        userId: Int,
        count: Int,
        page: Int,
        status: String
    ): List<AnimeRates> = dataSource.getUserAnimeList(
        accessToken = accessToken,
        userId = userId,
        count = count,
        page = page,
        status = status
    )

    override suspend fun createRate(userAgent: String, accessToken: String, userRate: UserRates) =
        dataSource.createRate(accessToken = accessToken, userRate = userRate)

    override suspend fun getHistory(accessToken: String, userId: Int, limit: Int): List<History> =
        dataSource.getHistory(accessToken = accessToken, userId = userId, limit = limit)
}
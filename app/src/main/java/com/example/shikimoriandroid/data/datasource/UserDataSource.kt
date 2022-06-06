package com.example.shikimoriandroid.data.datasource

import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.data.model.user.History
import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.data.network.ShikimoriUserApi
import javax.inject.Inject

class UserDataSource @Inject constructor(private val api: ShikimoriUserApi) {

    suspend fun getCurrentUser(accessToken: String): UserInfo =
        api.getCurrentUser(accessToken = accessToken)

    suspend fun getUserById(accessToken: String, id: Int): UserInfo =
        api.getUserById(id = id, accessToken = accessToken)

    suspend fun getUserAnimeList(
        accessToken: String,
        userId: Int,
        count: Int,
        page: Int,
        status: String
    ): List<AnimeRates> = api.getUserAnimeList(
        accessToken = accessToken,
        userId = userId,
        count = count,
        page = page,
        status = status
    )

    suspend fun createRate(accessToken: String, userRate: UserRates) =
        api.createRate(accessToken = accessToken, userRate = userRate)

    suspend fun getHistory(accessToken: String, userId: Int, limit: Int, page: Int): List<History> =
        api.getHistory(accessToken = accessToken, userId = userId, limit = limit, page = page)
}
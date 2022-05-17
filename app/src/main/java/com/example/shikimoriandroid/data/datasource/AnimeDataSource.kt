package com.example.shikimoriandroid.data.datasource

import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.data.network.ShikimoriAnimeApi
import javax.inject.Inject

class AnimeDataSource @Inject constructor(private val api: ShikimoriAnimeApi) {

    suspend fun getAnimes(
        count: Int,
        page: Int,
        sortBy: String,
        searchStr: String,
        genre: String
    ): List<AnimeInfo> =
        api.getAnimes(
            count = count,
            page = page,
            sortBy = sortBy,
            searchStr = searchStr,
            genre = genre
        )

    suspend fun getAnime(
        userAgent: String,
        accessToken: String,
        id: Int
    ): AnimeInfo = api.getAnime(
        userAgent = userAgent,
        accessToken = accessToken,
        id = id
    )
}
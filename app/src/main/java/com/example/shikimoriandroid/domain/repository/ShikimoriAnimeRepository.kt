package com.example.shikimoriandroid.domain.repository

import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.domain.utils.Constants

interface ShikimoriAnimeRepository {

    suspend fun getAnimes(
        count: Int,
        page: Int,
        sortBy: String,
        searchStr: String = "",
        genre: String = ""
    ): List<AnimeInfo>

    suspend fun getAnime(
        userAgent: String = Constants.appName,
        accessToken: String,
        id: Int
    ): AnimeInfo
}
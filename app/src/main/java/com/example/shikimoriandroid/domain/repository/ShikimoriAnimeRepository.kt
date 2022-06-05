package com.example.shikimoriandroid.domain.repository

import com.example.shikimoriandroid.data.model.anime.*
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

    suspend fun getRoles(id: Int): List<Role>

    suspend fun getCharacter(id: Int): CharacterInfo

    suspend fun getPerson(id: Int): PersonInfo

    suspend fun getScreenshots(id: Int): List<Screenshot>

    suspend fun getExternalLinks(id: Int): List<ExternalLink>
}
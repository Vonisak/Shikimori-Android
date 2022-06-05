package com.example.shikimoriandroid.data.datasource

import com.example.shikimoriandroid.data.model.anime.*
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

    suspend fun getRoles(id: Int): List<Role> = api.getRoles(id)

    suspend fun getCharacter(id: Int): CharacterInfo = api.getCharacter(id)

    suspend fun getPerson(id: Int): PersonInfo = api.getPerson(id)

    suspend fun getScreenshots(id: Int): List<Screenshot> = api.getScreenshots(id)

    suspend fun getExternalLinks(id: Int): List<ExternalLink> = api.getExternalLinks(id)
}
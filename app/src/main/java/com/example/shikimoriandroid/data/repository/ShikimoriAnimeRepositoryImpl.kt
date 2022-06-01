package com.example.shikimoriandroid.data.repository

import com.example.shikimoriandroid.data.datasource.AnimeDataSource
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.data.model.anime.CharacterInfo
import com.example.shikimoriandroid.data.model.anime.PersonInfo
import com.example.shikimoriandroid.data.model.anime.Role
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.domain.repository.ShikimoriAnimeRepository
import javax.inject.Inject

class ShikimoriAnimeRepositoryImpl @Inject constructor(private val dataSource: AnimeDataSource) :
    ShikimoriAnimeRepository {

    override suspend fun getAnimes(
        count: Int,
        page: Int,
        sortBy: String,
        searchStr: String,
        genre: String
    ): List<AnimeInfo> = dataSource.getAnimes(count, page, sortBy, searchStr, genre)


    override suspend fun getAnime(
        userAgent: String,
        accessToken: String,
        id: Int
    ): AnimeInfo = dataSource.getAnime(userAgent, accessToken, id)

    override suspend fun getRoles(id: Int): List<Role> = dataSource.getRoles(id)

    override suspend fun getCharacter(id: Int): CharacterInfo = dataSource.getCharacter(id)

    override suspend fun getPerson(id: Int): PersonInfo = dataSource.getPerson(id)
}
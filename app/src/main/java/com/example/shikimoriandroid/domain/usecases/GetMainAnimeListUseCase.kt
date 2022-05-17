package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.domain.repository.ShikimoriAnimeRepository
import javax.inject.Inject

class GetMainAnimeListUseCase @Inject constructor(private val repository: ShikimoriAnimeRepository) {

    suspend operator fun invoke(
        count: Int,
        page: Int,
        sortBy: String,
        searchStr: String,
        genre: String
    ): List<AnimeInfo> = repository.getAnimes(count, page, sortBy, searchStr, genre)
}
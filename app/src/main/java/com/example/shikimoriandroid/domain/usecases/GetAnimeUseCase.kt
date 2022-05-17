package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.domain.repository.ShikimoriAnimeRepository
import javax.inject.Inject

class GetAnimeUseCase @Inject constructor(private val repository: ShikimoriAnimeRepository) {

    suspend operator fun invoke(
        accessToken: String,
        id: Int
    ): AnimeInfo = repository.getAnime(accessToken = accessToken, id = id)
}
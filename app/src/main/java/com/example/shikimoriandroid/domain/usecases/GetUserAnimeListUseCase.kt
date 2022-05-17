package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.domain.repository.ShikimoriUserRepository
import javax.inject.Inject

class GetUserAnimeListUseCase @Inject constructor(private val repository: ShikimoriUserRepository) {

    suspend operator fun invoke(
        accessToken: String,
        userId: Int,
        count: Int,
        page: Int,
        status: String
    ): List<AnimeRates> = repository.getUserAnimeList(
        accessToken = accessToken,
        userId = userId,
        count = count,
        page = page,
        status = status
    )
}
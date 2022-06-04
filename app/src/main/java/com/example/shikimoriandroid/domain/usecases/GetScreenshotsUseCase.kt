package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.anime.Screenshot
import com.example.shikimoriandroid.domain.repository.ShikimoriAnimeRepository
import javax.inject.Inject

class GetScreenshotsUseCase @Inject constructor(private val repository: ShikimoriAnimeRepository) {

    suspend operator fun invoke(id: Int): List<Screenshot> = repository.getScreenshots(id)
}
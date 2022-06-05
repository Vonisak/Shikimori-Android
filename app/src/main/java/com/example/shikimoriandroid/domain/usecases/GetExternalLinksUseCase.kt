package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.anime.ExternalLink
import com.example.shikimoriandroid.domain.repository.ShikimoriAnimeRepository
import javax.inject.Inject

class GetExternalLinksUseCase @Inject constructor(private val repository: ShikimoriAnimeRepository) {

    suspend operator fun invoke(id: Int): List<ExternalLink> = repository.getExternalLinks(id)
}
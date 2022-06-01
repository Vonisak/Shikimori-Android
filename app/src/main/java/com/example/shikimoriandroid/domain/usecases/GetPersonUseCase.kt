package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.anime.PersonInfo
import com.example.shikimoriandroid.domain.repository.ShikimoriAnimeRepository
import javax.inject.Inject

class GetPersonUseCase @Inject constructor(private val repository: ShikimoriAnimeRepository) {

    suspend operator fun invoke(id: Int): PersonInfo = repository.getPerson(id)
}
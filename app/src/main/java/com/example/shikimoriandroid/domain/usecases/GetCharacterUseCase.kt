package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.anime.CharacterInfo
import com.example.shikimoriandroid.domain.repository.ShikimoriAnimeRepository
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(private val repository: ShikimoriAnimeRepository) {

    suspend operator fun invoke(id: Int): CharacterInfo = repository.getCharacter(id)
}
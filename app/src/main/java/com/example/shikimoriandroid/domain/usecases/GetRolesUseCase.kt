package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.anime.Role
import com.example.shikimoriandroid.domain.repository.ShikimoriAnimeRepository
import javax.inject.Inject

class GetRolesUseCase @Inject constructor(private val repository: ShikimoriAnimeRepository) {

    suspend operator fun invoke(id: Int): List<Role> = repository.getRoles(id)
}
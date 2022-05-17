package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.domain.repository.ShikimoriUserRepository
import javax.inject.Inject

class CreateRateUseCase @Inject constructor(private val repository: ShikimoriUserRepository) {

    suspend operator fun invoke(accessToken: String, userRate: UserRates) =
        repository.createRate(accessToken = accessToken, userRate = userRate)
}
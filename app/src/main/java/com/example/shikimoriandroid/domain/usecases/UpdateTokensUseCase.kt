package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.AuthResponse
import com.example.shikimoriandroid.domain.repository.ShikimoriAuthRepository
import javax.inject.Inject

class UpdateTokensUseCase @Inject constructor(private val repository: ShikimoriAuthRepository) {

    suspend operator fun invoke(refreshToken: String): AuthResponse =
        repository.updateTokens(refreshToken = refreshToken)
}
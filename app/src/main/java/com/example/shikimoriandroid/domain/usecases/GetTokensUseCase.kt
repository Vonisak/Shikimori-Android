package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.AuthResponse
import com.example.shikimoriandroid.domain.repository.ShikimoriAuthRepository
import javax.inject.Inject

class GetTokensUseCase @Inject constructor(private val repository: ShikimoriAuthRepository) {

    suspend operator fun invoke(authCode: String): AuthResponse =
        repository.getTokens(authCode = authCode)
}
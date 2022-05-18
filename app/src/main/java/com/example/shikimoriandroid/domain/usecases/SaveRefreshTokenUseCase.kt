package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.domain.repository.TokensSharedPrefRepository
import javax.inject.Inject

class SaveRefreshTokenUseCase @Inject constructor(private val repository: TokensSharedPrefRepository) {

    operator fun invoke(refreshToken: String) =
        repository.saveRefreshToken(refreshToken = refreshToken)
}
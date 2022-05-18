package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.domain.repository.TokensSharedPrefRepository
import javax.inject.Inject

class GetRefreshTokenUseCase @Inject constructor(private val repository: TokensSharedPrefRepository) {

    operator fun invoke(): String? = repository.getRefreshToken()
}
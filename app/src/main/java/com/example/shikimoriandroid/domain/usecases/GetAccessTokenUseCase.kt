package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.domain.repository.TokensSharedPrefRepository
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(private val repository: TokensSharedPrefRepository) {

    operator fun invoke(): String? = repository.getAccessToken()
}
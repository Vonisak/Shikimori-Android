package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.domain.repository.TokensSharedPrefRepository
import javax.inject.Inject

class SaveAccessTokenUseCase @Inject constructor(private val repository: TokensSharedPrefRepository) {

    operator fun invoke(accessToken: String) = repository.saveAccessToken(accessToken = accessToken)
}
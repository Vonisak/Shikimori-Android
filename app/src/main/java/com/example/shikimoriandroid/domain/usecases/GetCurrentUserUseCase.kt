package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.domain.repository.ShikimoriUserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val repository: ShikimoriUserRepository) {

    suspend operator fun invoke(accessToken: String): UserInfo =
        repository.getCurrentUser(accessToken = accessToken)
}
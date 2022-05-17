package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.domain.repository.ShikimoriUserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val repository: ShikimoriUserRepository) {

    suspend operator fun invoke(accessToken: String, id: Int): UserInfo =
        repository.getUserById(accessToken = accessToken, id = id)
}
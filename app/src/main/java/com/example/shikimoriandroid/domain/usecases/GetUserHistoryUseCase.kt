package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.data.model.user.History
import com.example.shikimoriandroid.domain.repository.ShikimoriUserRepository
import javax.inject.Inject

class GetUserHistoryUseCase @Inject constructor(private val repository: ShikimoriUserRepository) {

    suspend operator fun invoke(accessToken: String, userId: Int, limit: Int, page: Int): List<History> =
        repository.getHistory(accessToken, userId, limit, page)
}
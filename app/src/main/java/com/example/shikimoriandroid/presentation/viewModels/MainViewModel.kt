package com.example.shikimoriandroid.presentation.viewModels

import com.example.shikimoriandroid.domain.usecases.*
import com.example.shikimoriandroid.presentation.entity.State
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val updateTokensUseCase: UpdateTokensUseCase,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase,
    private val saveRefreshTokenUseCase: SaveRefreshTokenUseCase
) : NavigationModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    private fun handleError(error: Throwable) {
    }

    fun isUserAuth() : Boolean = getAccessTokenUseCase() != null

    fun updateUserTokens() {
        val refreshToken = getRefreshTokenUseCase()
        if (refreshToken != null) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val newTokens = updateTokensUseCase(refreshToken)
                saveAccessTokenUseCase(newTokens.accessToken)
                saveRefreshTokenUseCase(newTokens.refreshToken)
            }

        }
    }
}
package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.data.model.AuthResponse
import com.example.shikimoriandroid.domain.usecases.GetCurrentUserUseCase
import com.example.shikimoriandroid.domain.usecases.GetTokensUseCase
import com.example.shikimoriandroid.domain.usecases.SaveAccessTokenUseCase
import com.example.shikimoriandroid.domain.usecases.SaveRefreshTokenUseCase
import com.example.shikimoriandroid.presentation.entity.State
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getTokensUseCase: GetTokensUseCase,
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase,
    private val saveRefreshTokenUseCase: SaveRefreshTokenUseCase
) : NavigationModel() {

    private val _tokensState = MutableLiveData<State<AuthResponse>>()
    val tokensState: LiveData<State<AuthResponse>> = _tokensState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    fun getTokens(authCode: String) {
        _tokensState.postValue(State.Pending())

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            _tokensState.postValue(
                State.Success(
                    getTokensUseCase(authCode = authCode)
                )
            )
        }
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        saveAccessTokenUseCase(accessToken)
        saveRefreshTokenUseCase(refreshToken)
    }

    private fun handleError(error: Throwable) {
        _tokensState.postValue(State.Fail(error))
    }
}
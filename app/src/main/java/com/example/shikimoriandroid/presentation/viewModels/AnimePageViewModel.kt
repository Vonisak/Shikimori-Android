package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.domain.usecases.CreateRateUseCase
import com.example.shikimoriandroid.domain.usecases.GetAccessTokenUseCase
import com.example.shikimoriandroid.domain.usecases.GetAnimeUseCase
import com.example.shikimoriandroid.domain.usecases.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimePageViewModel @Inject constructor(
    private val getAnimeUseCase: GetAnimeUseCase,
    private val createRateUseCase: CreateRateUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : NavigationModel() {


    private val _animeInfoState = MutableLiveData<State<AnimeInfo>>()
    val animeInfoState: LiveData<State<AnimeInfo>> = _animeInfoState

    private val postUserRateState = MutableLiveData<State<String>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    fun getUserRateState(): LiveData<State<String>> = postUserRateState

    fun getAnime(id: Int) {
        _animeInfoState.postValue(State.Pending())

        var accessToken = getAccessTokenUseCase()

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (accessToken == null) accessToken = ""
            _animeInfoState.postValue(
                State.Success(
                    getAnimeUseCase(
                        id = id,
                        accessToken = "Bearer $accessToken"
                    )
                )
            )
        }
    }

    //TODO Подумай как сделать лучше
    fun postUserRate(userRates: UserRates) {
        postUserRateState.postValue(State.Pending())
        var accessToken = getAccessTokenUseCase()

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (accessToken == null) accessToken = ""
            val userRate =
                userRates.userRate.copy(userId = getCurrentUserUseCase("Bearer $accessToken").id)
            createRateUseCase(
                accessToken = "Bearer $accessToken",
                userRate = userRates.copy(userRate = userRate)
            )
        }
    }

    private fun handleError(error: Throwable) {
        _animeInfoState.postValue(State.Fail(error))
    }

    fun isUserAuth(): Boolean = getAccessTokenUseCase() != null
}
package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.domain.usecases.CreateRateUseCase
import com.example.shikimoriandroid.domain.usecases.GetAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimePageViewModel @Inject constructor(
    private val getAnimeUseCase: GetAnimeUseCase,
    private val createRateUseCase: CreateRateUseCase
) : ViewModel() {


    private val _animeInfoState = MutableLiveData<State<AnimeInfo>>()
    val animeInfoState: LiveData<State<AnimeInfo>> = _animeInfoState

    private val postUserRateState = MutableLiveData<State<String>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    fun getUserRateState(): LiveData<State<String>> = postUserRateState

    fun getAnime(id: Int, accessToken: String) {
        _animeInfoState.postValue(State.Pending())
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            _animeInfoState.postValue(
                State.Success(
                    getAnimeUseCase.invoke(
                        id = id,
                        accessToken = "Bearer $accessToken"
                    )
                )
            )
        }
    }

    fun postUserRate(accessToken: String, userRates: UserRates) {
        postUserRateState.postValue(State.Pending())
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            createRateUseCase.invoke(accessToken = "Bearer $accessToken", userRate = userRates)
        }
    }

    private fun handleError(error: Throwable) {
        _animeInfoState.postValue(State.Fail(error))
    }
}
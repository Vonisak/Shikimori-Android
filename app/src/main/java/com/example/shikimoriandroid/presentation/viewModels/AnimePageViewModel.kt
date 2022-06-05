package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.data.model.anime.ExternalLink
import com.example.shikimoriandroid.data.model.anime.Role
import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.domain.usecases.*
import com.example.shikimoriandroid.ui.utils.MutableLiveEvent
import com.example.shikimoriandroid.ui.utils.SingleLiveEvent
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
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getRolesUseCase: GetRolesUseCase,
    private val getExternalLinksUseCase: GetExternalLinksUseCase
) : NavigationModel() {

    private val _animeInfoState = MutableLiveData<State<AnimeInfo>>()
    val animeInfoState: LiveData<State<AnimeInfo>> = _animeInfoState

    private val _postUserRateState = SingleLiveEvent<State<String>>()
    val postUserRateState: LiveData<State<String>> = _postUserRateState

    private val _userAuth = MutableLiveData<Boolean>()
    val userAuth: LiveData<Boolean> = _userAuth

    private val _rolesState = MutableLiveData<State<List<Role>>>()
    val rolesState: LiveData<State<List<Role>>> = _rolesState

    private val _externalLinksState = MutableLiveData<State<List<ExternalLink>>>()
    val externalLinksState: LiveData<State<List<ExternalLink>>> = _externalLinksState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

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
        _postUserRateState.postValue(State.Pending())
        var accessToken = getAccessTokenUseCase()

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (accessToken == null) accessToken = ""
            val userRate =
                userRates.userRate.copy(userId = getCurrentUserUseCase("Bearer $accessToken").id)
            createRateUseCase(
                accessToken = "Bearer $accessToken",
                userRate = userRates.copy(userRate = userRate)
            )
            _postUserRateState.postValue(State.Success("Success"))
        }
    }

    fun getRoles(animeId: Int) {
        _rolesState.value = State.Pending()
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            _rolesState.postValue(State.Success(getRolesUseCase(animeId)))
        }
    }

    fun getExternalLinks(animeId: Int) {
        _externalLinksState.value = State.Pending()
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            _externalLinksState.postValue(State.Success(getExternalLinksUseCase(animeId)))
        }
    }

    private fun handleError(error: Throwable) {
        _animeInfoState.postValue(State.Fail(error))
    }

    fun checkUserAuth() {
        _userAuth.value = getAccessTokenUseCase() != null
    }
}
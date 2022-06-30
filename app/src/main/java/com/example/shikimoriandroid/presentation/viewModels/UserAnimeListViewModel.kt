package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.domain.repository.ShikimoriUserRepository
import com.example.shikimoriandroid.domain.usecases.GetAccessTokenUseCase
import com.example.shikimoriandroid.domain.usecases.GetCurrentUserUseCase
import com.example.shikimoriandroid.domain.usecases.GetUserAnimeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAnimeListViewModel @Inject constructor(
    private val getUserAnimeListUseCase: GetUserAnimeListUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) :
    NavigationModel() {

    private val _userListState = MutableLiveData<State<List<AnimeRates>>>()
    val userListState: LiveData<State<List<AnimeRates>>> = _userListState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    fun getUserAnimeRates(
        limit: Int,
        page: Int,
        status: String
    ) {
        _userListState.postValue(State.Pending())

        val accessToken = getAccessTokenUseCase()

        if (accessToken != null)
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val userId = getCurrentUserUseCase("Bearer $accessToken").id
            _userListState.postValue(
                State.Success(
                    getUserAnimeListUseCase(
                        accessToken = "Bearer $accessToken",
                        userId = userId,
                        count = limit,
                        page = page,
                        status = status
                    )
                )
            )
        }
    }

    private fun handleError(error: Throwable) {
        _userListState.postValue(State.Fail(error))
    }

}
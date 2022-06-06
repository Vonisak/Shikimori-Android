package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shikimoriandroid.data.model.user.History
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.domain.usecases.GetAccessTokenUseCase
import com.example.shikimoriandroid.domain.usecases.GetCurrentUserUseCase
import com.example.shikimoriandroid.domain.usecases.GetUserByIdUseCase
import com.example.shikimoriandroid.domain.usecases.GetUserHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserHistoryUseCase: GetUserHistoryUseCase
) : NavigationModel() {

    private val _userProfileState = MutableLiveData<State<UserInfo>>()
    val userProfileState: LiveData<State<UserInfo>> = _userProfileState

    private val _historyState = MutableLiveData<State<List<History>>>()
    val historyState: LiveData<State<List<History>>> = _historyState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    fun getProfileInfo() {
        _userProfileState.value = State.Pending()
        val accessToken = getAccessTokenUseCase()

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (accessToken != null) {
                val userId = getCurrentUserUseCase("Bearer $accessToken").id
                val user = getUserByIdUseCase(
                    accessToken = "Bearer $accessToken",
                    id = userId
                )
                _userProfileState.postValue(State.Success(user))
            } else {
                throw NullPointerException("Access token is null")
            }
        }

    }


    fun getUserHistory(limit: Int, page: Int) {
        _historyState.value = State.Pending()
        var accessToken = getAccessTokenUseCase()

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (accessToken == null) accessToken = ""
            val userId = getCurrentUserUseCase("Bearer $accessToken").id
            _historyState.postValue(
                State.Success(
                    getUserHistoryUseCase(
                        "Bearer $accessToken",
                        userId,
                        limit,
                        page
                    )
                )
            )
        }
    }

    private fun handleError(error: Throwable) {
        _userProfileState.postValue(State.Fail(error))
    }
}
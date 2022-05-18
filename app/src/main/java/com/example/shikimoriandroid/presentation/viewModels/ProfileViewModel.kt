package com.example.shikimoriandroid.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.domain.usecases.GetAccessTokenUseCase
import com.example.shikimoriandroid.domain.usecases.GetCurrentUserUseCase
import com.example.shikimoriandroid.domain.usecases.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) :
    ViewModel() {

    private val _userProfileState = MutableLiveData<State<UserInfo>>()
    val userProfileState: LiveData<State<UserInfo>> = _userProfileState

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

    private fun handleError(error: Throwable) {
        _userProfileState.postValue(State.Fail(error))
    }
}
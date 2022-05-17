package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.domain.usecases.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val getUserByIdUseCase: GetUserByIdUseCase) :
    ViewModel() {

    private val _userProfileState = MutableLiveData<State<UserInfo>>()
    val userProfileState: LiveData<State<UserInfo>> = _userProfileState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }


    fun getProfileInfo(accessToken: String, userId: Int) {
        _userProfileState.postValue(State.Pending())
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            _userProfileState.postValue(
                State.Success(
                    getUserByIdUseCase.invoke(accessToken = accessToken, id = userId)
                )
            )
        }
    }

    private fun handleError(error: Throwable) {
        _userProfileState.postValue(State.Fail(error))
    }
}
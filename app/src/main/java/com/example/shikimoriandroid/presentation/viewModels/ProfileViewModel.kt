package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.domain.entity.State
import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.data.datasource.retrofit.ShikimoriAPI
import com.example.shikimoriandroid.data.datasource.retrofit.ShikimoriRetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileViewModel: ViewModel() {

    private val provider: ShikimoriAPI = ShikimoriRetrofitClient.retrofit.create(
        ShikimoriAPI::class.java
    )
    private val state = MutableLiveData<State<UserInfo>>()

    fun getState(): LiveData<State<UserInfo>> = state

    fun getProfileInfo(accessToken: String, userId: Int) {
        state.postValue(State.Pending())
        provider.getUserById(accessToken = "Bearer $accessToken", id = userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    state.postValue(State.Success(it))
                },
                {
                    state.postValue(State.Fail(it))
                }
            )
    }
}
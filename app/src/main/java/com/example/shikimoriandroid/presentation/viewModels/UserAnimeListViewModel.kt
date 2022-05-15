package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.domain.entity.State
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.data.datasource.retrofit.ShikimoriAPI
import com.example.shikimoriandroid.data.datasource.retrofit.ShikimoriRetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class UserAnimeListViewModel: ViewModel() {
    private val provider: ShikimoriAPI = ShikimoriRetrofitClient.retrofit.create(
        ShikimoriAPI::class.java
    )
    private val state = MutableLiveData<State<List<AnimeRates>>>()

    fun getState(): LiveData<State<List<AnimeRates>>> = state

    fun getUserAnimeRates(
        accessToken: String,
        userId: Int,
        limit: Int,
        page: Int,
        status: String) {
        state.postValue(State.Pending())
        provider.getUserAnimeList(
            accessToken = "Bearer $accessToken",
            userId = userId,
            count = limit,
            page = page,
            status = status
        )
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
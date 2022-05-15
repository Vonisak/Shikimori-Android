package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.domain.entity.State
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.data.datasource.retrofit.ShikimoriAPI
import com.example.shikimoriandroid.data.datasource.retrofit.ShikimoriRetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AnimePageViewModel: ViewModel() {

    private val provider: ShikimoriAPI = ShikimoriRetrofitClient.retrofit.create(
        ShikimoriAPI::class.java
    )
    private val animeInfoState = MutableLiveData<State<AnimeInfo>>()
    private val postUserRateState = MutableLiveData<State<String>>()

    fun getAnimeInfoState(): LiveData<State<AnimeInfo>> = animeInfoState
    fun getUserRateState(): LiveData<State<String>> = postUserRateState

    fun getAnime(id: Int, accessToken: String) {
        animeInfoState.postValue(State.Pending())
        provider.getAnime(id = id, accessToken = "Bearer $accessToken")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    animeInfoState.postValue(State.Success(it))
                },
                {
                    animeInfoState.postValue(State.Fail(it))
                }
            )
    }

    fun postUserRate(accessToken: String, userRates: UserRates) {
        postUserRateState.postValue(State.Pending())
        provider.createRate(
            accessToken = "Bearer $accessToken",
            userRate = userRates
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                {
                    postUserRateState.postValue(State.Fail(it))
                }
            )
    }
}
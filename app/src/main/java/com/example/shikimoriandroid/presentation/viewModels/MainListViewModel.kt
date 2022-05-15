package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.domain.entity.State
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.data.datasource.retrofit.ShikimoriAPI
import com.example.shikimoriandroid.data.datasource.retrofit.ShikimoriRetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainListViewModel: ViewModel() {
    private val provider: ShikimoriAPI = ShikimoriRetrofitClient.retrofit.create(
    ShikimoriAPI::class.java
    )
    private val state = MutableLiveData<State<List<AnimeInfo>>>()

    fun getState(): LiveData<State<List<AnimeInfo>>> = state

    fun getAnimeList(count: Int, page: Int, order: String, searchStr: String = "", genre: String = "") {
        state.postValue(State.Pending())
        provider.getAnimes(count, page, order, searchStr, genre)
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
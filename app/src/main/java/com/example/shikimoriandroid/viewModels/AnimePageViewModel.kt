package com.example.shikimoriandroid.viewModels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.State
import com.example.shikimoriandroid.model.AnimeInfo
import com.example.shikimoriandroid.retrofit.ShikimoriAPI
import com.example.shikimoriandroid.retrofit.ShikimoriRetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AnimePageViewModel: ViewModel() {

    private val provider: ShikimoriAPI = ShikimoriRetrofitClient.retrofit.create(
        ShikimoriAPI::class.java
    )
    private val handler = Handler(Looper.getMainLooper())
    private val state = MutableLiveData<State<AnimeInfo>>()

    fun getState(): LiveData<State<AnimeInfo>> = state

    fun getAnime(id: Int) {
        state.postValue(State.Pending())
        provider.getAnime(id)
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
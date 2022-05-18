package com.example.shikimoriandroid.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.domain.usecases.GetMainAnimeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor(private val getMainAnimeListUseCase: GetMainAnimeListUseCase) :
    ViewModel() {

    private val _mainAnimeListState = MutableLiveData<State<List<AnimeInfo>>>()
    val mainAnimeListState: LiveData<State<List<AnimeInfo>>> = _mainAnimeListState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    fun getAnimeList(
        count: Int,
        page: Int,
        order: String,
        searchStr: String = "",
        genre: String = ""
    ) {
        _mainAnimeListState.value = State.Pending()
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            _mainAnimeListState.postValue(
                State.Success(
                    getMainAnimeListUseCase(
                        count = count,
                        page = page,
                        sortBy = order,
                        searchStr = searchStr,
                        genre = genre
                    )
                )
            )
        }
        Log.i("TAG", "null is ${_mainAnimeListState.value.toString()}")
    }

    private fun handleError(error: Throwable) {
        _mainAnimeListState.postValue(State.Fail(error))
    }
}
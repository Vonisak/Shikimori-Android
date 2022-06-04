package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.data.model.anime.Screenshot
import com.example.shikimoriandroid.domain.usecases.GetScreenshotsUseCase
import com.example.shikimoriandroid.presentation.entity.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenshotsViewModel @Inject constructor(private val getScreenshotsUseCase: GetScreenshotsUseCase) :
    ViewModel() {

    private val _screenshotsState = MutableLiveData<State<List<Screenshot>>>()
    val screenshotsState: LiveData<State<List<Screenshot>>> = _screenshotsState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    private fun handleError(error: Throwable) {
        _screenshotsState.postValue(State.Fail(error))
    }

    fun getScreenshots(animeId: Int) {
        _screenshotsState.value = State.Pending()
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            _screenshotsState.postValue(State.Success(getScreenshotsUseCase(animeId)))
        }
    }
}
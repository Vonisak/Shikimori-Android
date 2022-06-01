package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.data.model.anime.CharacterInfo
import com.example.shikimoriandroid.domain.usecases.GetCharacterUseCase
import com.example.shikimoriandroid.presentation.entity.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val getCharacterUseCase: GetCharacterUseCase) :
    ViewModel() {

    private val _characterState = MutableLiveData<State<CharacterInfo>>()
    val characterState: LiveData<State<CharacterInfo>> = _characterState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    private fun handleError(error: Throwable) {
        _characterState.postValue(State.Fail(error))
    }

    fun getCharacter(id: Int) {
        _characterState.value = State.Pending()
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            _characterState.postValue(State.Success(getCharacterUseCase(id)))
        }
    }
}
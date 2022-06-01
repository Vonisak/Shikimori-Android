package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.data.model.anime.PersonInfo
import com.example.shikimoriandroid.domain.usecases.GetPersonUseCase
import com.example.shikimoriandroid.presentation.entity.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(private val getPersonUseCase: GetPersonUseCase) :
    ViewModel() {

    private val _personState = MutableLiveData<State<PersonInfo>>()
    val personState: LiveData<State<PersonInfo>> = _personState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    private fun handleError(error: Throwable) {
        _personState.postValue(State.Fail(error))
    }

    fun getPerson(id: Int) {
        _personState.value = State.Pending()
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            _personState.postValue(State.Success(getPersonUseCase(id)))
        }
    }
}
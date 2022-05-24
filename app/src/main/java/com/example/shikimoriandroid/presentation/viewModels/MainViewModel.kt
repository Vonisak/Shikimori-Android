package com.example.shikimoriandroid.presentation.viewModels

import com.example.shikimoriandroid.domain.usecases.GetAccessTokenUseCase
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : NavigationModel() {

    fun isUserAuth() : Boolean = getAccessTokenUseCase() != null

}
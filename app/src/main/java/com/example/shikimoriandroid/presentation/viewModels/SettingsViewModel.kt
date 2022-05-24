package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.shikimoriandroid.domain.usecases.ClearTokensUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val clearTokensUseCase: ClearTokensUseCase) :
    ViewModel() {

    fun profileExit() = clearTokensUseCase()
}
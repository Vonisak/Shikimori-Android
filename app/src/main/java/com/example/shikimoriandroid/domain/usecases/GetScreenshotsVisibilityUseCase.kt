package com.example.shikimoriandroid.domain.usecases

import com.example.shikimoriandroid.domain.repository.SettingsRepository
import javax.inject.Inject

class GetScreenshotsVisibilityUseCase @Inject constructor(private val repository: SettingsRepository) {

    operator fun invoke(): Boolean = repository.getScreenshotsVisibility()
}
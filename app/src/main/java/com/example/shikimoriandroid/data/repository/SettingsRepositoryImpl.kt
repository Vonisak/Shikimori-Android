package com.example.shikimoriandroid.data.repository

import com.example.shikimoriandroid.data.datasource.SettingsDataSource
import com.example.shikimoriandroid.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val datasource: SettingsDataSource) :
    SettingsRepository {

    override fun getScreenshotsVisibility(): Boolean = datasource.getScreenshotsVisibility()

    override fun getDescriptionVisibility(): Boolean = datasource.getDescriptionVisibility()
}
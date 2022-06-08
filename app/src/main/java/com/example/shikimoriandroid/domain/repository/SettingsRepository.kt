package com.example.shikimoriandroid.domain.repository

interface SettingsRepository {

    fun getScreenshotsVisibility(): Boolean

    fun getDescriptionVisibility(): Boolean
}
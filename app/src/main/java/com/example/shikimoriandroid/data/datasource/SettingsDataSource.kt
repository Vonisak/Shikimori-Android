package com.example.shikimoriandroid.data.datasource

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.shikimoriandroid.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsDataSource @Inject constructor(@ApplicationContext private val context: Context) {

    fun getScreenshotsVisibility(): Boolean =
        PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            context.resources.getString(
                R.string.screenshots_id
            ), true
        )

    fun getDescriptionVisibility(): Boolean =
        PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            context.resources.getString(
                R.string.description_id
            ), true
        )
}
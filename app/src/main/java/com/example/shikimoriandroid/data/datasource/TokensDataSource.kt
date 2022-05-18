package com.example.shikimoriandroid.data.datasource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokensDataSource @Inject constructor(@ApplicationContext private val context: Context) {

    private companion object {
        const val APP_PREFERENCES = "tokens"

        const val APP_PREFERENCES_ACCESS_TOKEN_KEY = "access_token_key"

        const val APP_PREFERENCES_REFRESH_TOKEN_KEY = "refresh_token_key"
    }

    fun saveAccessToken(accessToken: String) {
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putString(APP_PREFERENCES_ACCESS_TOKEN_KEY, accessToken)
            .apply()
    }

    fun getAccessToken(): String? =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(APP_PREFERENCES_ACCESS_TOKEN_KEY, "null")


    fun saveRefreshToken(refreshToken: String) {
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putString(APP_PREFERENCES_REFRESH_TOKEN_KEY, refreshToken)
            .apply()
    }

    fun getRefreshToken(): String? =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(APP_PREFERENCES_REFRESH_TOKEN_KEY, "null")

    fun clearSharedPref() {
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

}
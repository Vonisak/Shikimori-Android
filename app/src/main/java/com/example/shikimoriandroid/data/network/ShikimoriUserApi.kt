package com.example.shikimoriandroid.data.network

import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.data.model.user.History
import com.example.shikimoriandroid.data.model.user.UserInfo
import com.example.shikimoriandroid.domain.utils.Constants
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface ShikimoriUserApi {
    @GET("api/users/whoami")
    suspend fun getCurrentUser(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String
    ): UserInfo

    @GET("api/users/{id}")
    suspend fun getUserById(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int
    ): UserInfo

    @GET("api/users/{user_id}/anime_rates")
    suspend fun getUserAnimeList(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String,
        @Path("user_id") userId: Int,
        @Query("limit") count: Int,
        @Query("page") page: Int,
        @Query("status") status: String
    ): List<AnimeRates>

    @POST("api/v2/user_rates")
    suspend fun createRate(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String,
        @Body userRate: UserRates
    )

    @GET("/api/users/{user_id}/history")
    suspend fun getHistory(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String,
        @Path("user_id") userId: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): List<History>
}
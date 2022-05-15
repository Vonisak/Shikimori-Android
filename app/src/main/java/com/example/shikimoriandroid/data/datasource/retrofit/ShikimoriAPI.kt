package com.example.shikimoriandroid.data.datasource.retrofit

import com.example.shikimoriandroid.domain.utils.Constants
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.data.model.AuthResponse
import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.data.model.user.UserInfo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface ShikimoriAPI {
    @GET("api/animes")
    fun getAnimes(
        @Query("limit") count: Int,
        @Query("page") page: Int,
        @Query("order") sortBy: String,
        @Query("search") searchStr: String = "",
        @Query("genre") genre: String = ""
    ) : Observable<List<AnimeInfo>>

    @GET("api/animes/{id}")
    fun getAnime(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int
    ) : Observable<AnimeInfo>

    @FormUrlEncoded
    @POST("oauth/token")
    fun getTokens(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("client_id") clientId: String = Constants.clientId,
        @Field("client_secret") clientSecret: String = Constants.clientSecret,
        @Field("code") authCode: String,
        @Field("redirect_uri") redirectUri: String = "urn:ietf:wg:oauth:2.0:oob"
    ) : Observable<AuthResponse>

    @GET("api/users/whoami")
    fun getCurrentUser(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String
    ) : Observable<UserInfo>

    @GET("api/users/{id}")
    fun getUserById(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int
    ) : Observable<UserInfo>

    @GET("api/users/{user_id}/anime_rates")
    fun getUserAnimeList(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String,
        @Path("user_id") userId: Int,
        @Query("limit") count: Int,
        @Query("page") page: Int,
        @Query("status") status: String
    ) : Observable<List<AnimeRates>>

    @POST("api/v2/user_rates")
    fun createRate(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String,
        @Body userRate: UserRates
    ) : Completable
}
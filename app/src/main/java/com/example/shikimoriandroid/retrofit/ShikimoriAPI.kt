package com.example.shikimoriandroid.retrofit

import com.example.shikimoriandroid.Constants
import com.example.shikimoriandroid.model.AnimeInfo
import com.example.shikimoriandroid.model.AuthResponse
import com.example.shikimoriandroid.model.UserInfo
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
}
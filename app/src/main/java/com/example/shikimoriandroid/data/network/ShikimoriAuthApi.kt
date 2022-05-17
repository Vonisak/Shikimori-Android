package com.example.shikimoriandroid.data.network

import com.example.shikimoriandroid.data.model.AuthResponse
import com.example.shikimoriandroid.domain.utils.Constants
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ShikimoriAuthApi {
    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getTokens(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("client_id") clientId: String = Constants.clientId,
        @Field("client_secret") clientSecret: String = Constants.clientSecret,
        @Field("code") authCode: String,
        @Field("redirect_uri") redirectUri: String = "urn:ietf:wg:oauth:2.0:oob"
    ): AuthResponse
}
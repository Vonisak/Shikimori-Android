package com.example.shikimoriandroid.data.network

import com.example.shikimoriandroid.domain.utils.Constants
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import retrofit2.Response
import retrofit2.http.*

interface ShikimoriAnimeApi {
    @GET("api/animes")
    suspend fun getAnimes(
        @Query("limit") count: Int,
        @Query("page") page: Int,
        @Query("order") sortBy: String,
        @Query("search") searchStr: String = "",
        @Query("genre") genre: String = ""
    ): List<AnimeInfo>

    @GET("api/animes/{id}")
    suspend fun getAnime(
        @Header("User-Agent") userAgent: String = Constants.appName,
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int
    ): AnimeInfo
}
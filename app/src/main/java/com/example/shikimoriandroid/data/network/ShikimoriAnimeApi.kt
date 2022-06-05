package com.example.shikimoriandroid.data.network

import com.example.shikimoriandroid.data.model.anime.*
import com.example.shikimoriandroid.domain.utils.Constants
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

    @GET("api/animes/{id}/roles")
    suspend fun getRoles(
        @Path("id") id: Int
    ): List<Role>

    @GET("api/characters/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): CharacterInfo

    @GET("api/people/{id}")
    suspend fun getPerson(
        @Path("id") id: Int
    ): PersonInfo

    @GET("api/animes/{id}/screenshots")
    suspend fun getScreenshots(
        @Path("id") id: Int
    ): List<Screenshot>

    @GET("api/animes/{id}/external_links")
    suspend fun getExternalLinks(
        @Path("id") id: Int
    ): List<ExternalLink>
}
package com.example.shikimoriandroid.di

import com.example.shikimoriandroid.data.network.ShikimoriAnimeApi
import com.example.shikimoriandroid.data.network.ShikimoriAuthApi
import com.example.shikimoriandroid.data.network.ShikimoriUserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    private companion object {

        const val BASE_URL = "https://shikimori.one/"
    }

    @Provides
    @ShikimoriBaseUrl
    fun provideBaseUrl(): String =
        BASE_URL

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        @ShikimoriBaseUrl baseUrl: String,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideRetrofitAnimeApi(retrofit: Retrofit): ShikimoriAnimeApi = retrofit.create(
        ShikimoriAnimeApi::class.java
    )

    @Provides
    @Singleton
    fun provideRetrofitAuthApi(retrofit: Retrofit): ShikimoriAuthApi = retrofit.create(
        ShikimoriAuthApi::class.java
    )

    @Provides
    @Singleton
    fun provideRetrofitUserApi(retrofit: Retrofit): ShikimoriUserApi = retrofit.create(
        ShikimoriUserApi::class.java
    )

}
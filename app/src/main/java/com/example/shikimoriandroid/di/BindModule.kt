package com.example.shikimoriandroid.di

import com.example.shikimoriandroid.data.repository.*
import com.example.shikimoriandroid.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun bindAnimeRepository(impl: ShikimoriAnimeRepositoryImpl): ShikimoriAnimeRepository

    @Binds
    fun bindAuthRepository(impl: ShikimoriAuthRepositoryImpl): ShikimoriAuthRepository

    @Binds
    fun bindUserRepository(impl: ShikimoriUserRepositoryImpl): ShikimoriUserRepository

    @Binds
    fun bindTokenRepository(impl: TokensSharedPrefRepositoryImpl): TokensSharedPrefRepository

    @Binds
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
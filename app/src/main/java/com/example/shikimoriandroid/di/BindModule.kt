package com.example.shikimoriandroid.di

import com.example.shikimoriandroid.data.repository.ShikimoriAnimeRepositoryImpl
import com.example.shikimoriandroid.data.repository.ShikimoriAuthRepositoryImpl
import com.example.shikimoriandroid.data.repository.ShikimoriUserRepositoryImpl
import com.example.shikimoriandroid.domain.repository.ShikimoriAnimeRepository
import com.example.shikimoriandroid.domain.repository.ShikimoriAuthRepository
import com.example.shikimoriandroid.domain.repository.ShikimoriUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun bindAnimeRepository(repository: ShikimoriAnimeRepositoryImpl): ShikimoriAnimeRepository

    @Binds
    fun bindAuthRepository(repository: ShikimoriAuthRepositoryImpl): ShikimoriAuthRepository

    @Binds
    fun bindUserRepository(repository: ShikimoriUserRepositoryImpl): ShikimoriUserRepository
}
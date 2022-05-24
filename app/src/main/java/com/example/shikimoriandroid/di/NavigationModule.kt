package com.example.shikimoriandroid.di

import com.github.terrakok.cicerone.Cicerone
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NavigationModule {
    private val cicerone = Cicerone.create()

    @Singleton
    @Provides
    fun provideRouter() = cicerone.router

    @Singleton
    @Provides
    fun provideNavigatorHolder() = cicerone.getNavigatorHolder()
}
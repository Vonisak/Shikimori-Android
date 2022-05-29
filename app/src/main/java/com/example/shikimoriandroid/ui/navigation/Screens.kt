package com.example.shikimoriandroid.ui.navigation

import com.example.shikimoriandroid.ui.fragments.*
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun profile() = FragmentScreen { ProfileFragment() }

    fun mainList(genre: String) = FragmentScreen { MainAnimeListFragment(genre) }

    fun mainList() = FragmentScreen { MainAnimeListFragment() }

    fun settings() = FragmentScreen { SettingsFragment() }

    fun auth() = FragmentScreen { AuthFragment() }

    fun animePage(animeId: Int) = FragmentScreen { AnimePageFragment(animeId) }

    fun userList() = FragmentScreen { UserListFragment() }
}
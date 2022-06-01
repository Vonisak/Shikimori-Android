package com.example.shikimoriandroid.ui.navigation

import com.example.shikimoriandroid.data.model.anime.Role
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

    fun personList(persons: List<Role>) = FragmentScreen { PersonListFragment(persons) }

    fun characterList(characters: List<Role>) = FragmentScreen { CharacterListFragment(characters) }

    fun characterInfo(id: Int) = FragmentScreen { CharacterInfoFragment(id) }

    fun personInfo(id: Int) = FragmentScreen { PersonInfoFragment(id) }
}
package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import javax.inject.Inject

open class NavigationModel: ViewModel() {

    @Inject
    lateinit var router: Router

    fun navigateTo(screen: Screen) {
        router.navigateTo(screen)
    }

    fun back() {
        router.exit()
    }

    fun replaceScreen(screen: Screen) {
        router.replaceScreen(screen)
    }
}
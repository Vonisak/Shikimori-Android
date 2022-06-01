package com.example.shikimoriandroid.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class NavigationModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var router: Router

    fun navigateTo(screen: Screen) {
        router.navigateTo(screen)
    }

    fun back() {
        router.exit()
    }

    fun newRootScreen(screen: Screen) {
        router.newRootScreen(screen)
    }

    fun backTo(screen: Screen) {
        router.backTo(screen)
    }

    fun replaceScreen(screen: Screen) {
        router.replaceScreen(screen)
    }
}
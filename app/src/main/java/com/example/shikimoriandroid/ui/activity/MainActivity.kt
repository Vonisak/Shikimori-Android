package com.example.shikimoriandroid.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.shikimoriandroid.DatabaseApplication
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.ActivityMainBinding
import com.example.shikimoriandroid.data.datasource.localBd.AuthDao
import com.example.shikimoriandroid.data.datasource.localBd.AuthInfo
import com.example.shikimoriandroid.presentation.viewModels.MainViewModel
import com.example.shikimoriandroid.ui.navigation.Screens
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val deck = ArrayDeque<Int>()
    private lateinit var reselectedListener: ItemReselectedListener
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = AppNavigator(this, R.id.nav_host_fragment)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.mainToolbar.root)
        supportActionBar?.title = "Список"
        deck.add(R.id.anime_list_item)
        setContentView(binding.root)
        navSettings()

        navigatorHolder.setNavigator(navigator)
    }

    private fun navSettings() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.anime_list_item -> {
                    supportActionBar?.title = "Список"
                    viewModel.navigateTo(Screens.mainList())
                    deck.add(R.id.anime_list_item)
                    true
                }
                R.id.anime_profile_item -> {
                    if (viewModel.isUserAuth()) {
                        supportActionBar?.title = "Профиль"
                        viewModel.navigateTo(Screens.profile())
                    } else {
                        supportActionBar?.title = "Авторизация"
                        viewModel.navigateTo(Screens.auth())
                    }
                    deck.add(R.id.anime_profile_item)
                    true
                }
                R.id.anime_settings_item -> {
                    supportActionBar?.title = "Настройки"
                    viewModel.navigateTo(Screens.settings())
                    deck.add(R.id.anime_settings_item)
                    true
                }
                else -> false
            }

        }
        binding.bottomNavigation.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.anime_list_item -> reselectedListener.reselect()
            }
        }
    }

    // Метод делать видимым/невидимым BottomNavigationBar
    //TODO добавить анимацию
    fun showBottomNav(visible: Boolean) {
        if (visible) {
            binding.bottomNavigation.visibility = View.VISIBLE
        } else {
            binding.bottomNavigation.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        if (deck.size > 1) {
            binding.bottomNavigation.menu.findItem(deck.removeLast()).isChecked =
                false
            binding.bottomNavigation.menu.findItem(deck.last()).isChecked = true
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    fun setItemReselectedListener(listener: ItemReselectedListener) {
        reselectedListener = listener
    }

    interface ItemReselectedListener {
        fun reselect()
    }

}
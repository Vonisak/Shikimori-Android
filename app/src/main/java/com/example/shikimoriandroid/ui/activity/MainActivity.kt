package com.example.shikimoriandroid.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.ActivityMainBinding
import com.example.shikimoriandroid.presentation.viewModels.MainViewModel
import com.example.shikimoriandroid.ui.fragments.MainAnimeListFragment
import com.example.shikimoriandroid.ui.navigation.Screens
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var reselectedListener: ItemReselectedListener
    private var onBackListener: BackListener? = null
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = AppNavigator(this, R.id.nav_host_fragment)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.mainToolbar.root)
        supportActionBar?.title = MainAnimeListFragment.TITLE
        setContentView(binding.root)
        navSettings()

        navigatorHolder.setNavigator(navigator)
    }

    private fun navSettings() {
        binding.bottomNavigation.setOnItemSelectedListener {
            viewModel.newRootScreen(Screens.mainList())
            when (it.itemId) {
                R.id.anime_list_item -> {
                    viewModel.backTo(Screens.mainList())
                    binding.bottomNavigation.menu.findItem(R.id.anime_list_item).isChecked = true
                    true
                }
                R.id.anime_profile_item -> {
                    binding.bottomNavigation.menu.findItem(R.id.anime_profile_item).isChecked = true
                    if (viewModel.isUserAuth()) {
                        viewModel.navigateTo(Screens.profile())
                    } else {
                        viewModel.navigateTo(Screens.auth())
                    }
                    true
                }
                R.id.anime_settings_item -> {
                    binding.bottomNavigation.menu.findItem(R.id.anime_settings_item).isChecked =
                        true
                    viewModel.navigateTo(Screens.settings())
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
    fun showBottomNav(visible: Boolean) {
        if (visible) {
            binding.bottomNavigation.visibility = View.VISIBLE
        } else {
            binding.bottomNavigation.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        onBackListener?.onBack()
        viewModel.back()
    }

    fun bottomNavChecked(itemId: Int, checked: Boolean) {
        binding.bottomNavigation.menu.findItem(itemId).isChecked = checked
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    fun setItemReselectedListener(listener: ItemReselectedListener) {
        reselectedListener = listener
    }

    fun setOnBackListener(listener: BackListener?) {
        onBackListener = listener
    }

    interface BackListener {
        fun onBack()
    }

    interface ItemReselectedListener {
        fun reselect()
    }

}
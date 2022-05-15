package com.example.shikimoriandroid.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.shikimoriandroid.DatabaseApplication
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.ActivityMainBinding
import com.example.shikimoriandroid.data.datasource.localBd.AuthDao
import com.example.shikimoriandroid.data.datasource.localBd.AuthInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val deck = ArrayDeque<Int>()
    private lateinit var reselectedListener: ItemReselectedListener
    lateinit var users: List<AuthInfo>
    lateinit var userDao: AuthDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.mainToolbar.root)
        supportActionBar?.title = "Список"
        deck.add(R.id.anime_list_item)
        setContentView(binding.root)
        navSettings()
        updateUsersInfo()
    }

    private fun navSettings() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.anime_list_item -> {
                    supportActionBar?.title = "Список"
                    findNavController(R.id.nav_host_fragment).navigate(R.id.mainAnimeListFragment)
                    deck.add(R.id.anime_list_item)
                    true
                }
                R.id.anime_profile_item -> {
                    if (users.isEmpty()) {
                        supportActionBar?.title = "Авторизация"
                        findNavController(R.id.nav_host_fragment).navigate(R.id.authFragment)
                    } else {
                        supportActionBar?.title = "Профиль"
                        findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                    }
                    deck.add(R.id.anime_profile_item)
                    true
                }
                R.id.anime_settings_item -> {
                    supportActionBar?.title = "Настройки"
                    findNavController(R.id.nav_host_fragment).navigate(R.id.settingsFragment)
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

    fun updateUsersInfo() {
        userDao = (applicationContext as DatabaseApplication).database.authDao()
        runBlocking(Dispatchers.IO) {
            users = userDao.getAll()
            Log.i("TAG", users.toString())
        }
    }

}
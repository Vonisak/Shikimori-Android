package com.example.shikimoriandroid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.example.shikimoriandroid.MainActivity
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.FragmentMainAnimeListBinding
import com.example.shikimoriandroid.databinding.FragmentSettingsBinding
import com.example.shikimoriandroid.localBd.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class SettingsFragment : BaseBottomNavFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.signOutButton.setOnClickListener {
            runBlocking(Dispatchers.IO) {
                (activity as MainActivity).userDao.cleanUsers()
            }
            (activity as MainActivity).updateUsersInfo()
        }

        return binding.root
    }
}
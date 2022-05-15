package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.databinding.FragmentSettingsBinding
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
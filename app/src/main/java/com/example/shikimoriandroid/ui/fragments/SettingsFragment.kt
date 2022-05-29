package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.databinding.FragmentSettingsBinding
import com.example.shikimoriandroid.presentation.viewModels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class SettingsFragment : BaseBottomNavFragment() {

    companion object {

        const val TITLE = "Настройки"
    }

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar?.title = TITLE
        binding.signOutButton.setOnClickListener {
            viewModel.profileExit()
        }

        return binding.root
    }
}
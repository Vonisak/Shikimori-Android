package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.presentation.viewModels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    companion object {

        const val TITLE = "Настройки"
    }

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = TITLE
        initListeners()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    private fun initListeners() {
        findPreference<Preference>("exit")?.setOnPreferenceClickListener {
            viewModel.profileExit()
            true
        }
    }

}
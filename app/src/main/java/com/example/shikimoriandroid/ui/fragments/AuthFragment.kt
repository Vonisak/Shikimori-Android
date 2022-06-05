package com.example.shikimoriandroid.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.domain.utils.Constants
import com.example.shikimoriandroid.databinding.FragmentAuthBinding
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.presentation.viewModels.AuthViewModel
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.ui.navigation.Screens
import com.example.shikimoriandroid.ui.utils.shakeAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseBottomNavFragment() {

    companion object {

        const val TITLE = "Авторизация"
    }

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar?.title = TITLE

        observeModel()
        initListeners()

        return binding.root
    }

    private fun initListeners() {
        binding.authButton.setOnClickListener {
            val authCode = binding.authInput.text.toString()
            if (authCode == "") {
                binding.textInputLayout.shakeAnimation()
                binding.authInput.error = getString(R.string.empty_view)
            } else {
                viewModel.getTokens(authCode)
            }
        }

        binding.auth.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.appLink)
            startActivity(intent)
        }
    }

    private fun observeModel() {
        viewModel.tokensState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Pending -> {
                }
                is State.Success -> {
                    viewModel.saveTokens(state.data.accessToken, state.data.refreshToken)
                    viewModel.replaceScreen(Screens.profile())
                }
                is State.Fail -> {
                    binding.textInputLayout.shakeAnimation()
                    binding.authInput.error = state.error?.message
                }
            }
        }
    }
}
package com.example.shikimoriandroid.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.domain.utils.Constants
import com.example.shikimoriandroid.databinding.FragmentAuthBinding
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.presentation.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class AuthFragment : BaseBottomNavFragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        observeModel()
        initListeners()

        return binding.root
    }

    private fun initListeners() {
        binding.authButton.setOnClickListener {
            val authCode = binding.authInput.text.toString()
            viewModel.getTokens(authCode)
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
                is State.Pending -> {}
                is State.Success -> {
                    viewModel.saveTokens(state.data.accessToken, state.data.refreshToken)
                    navigate(R.id.action_authFragment_to_profileFragment)
                    findNavController().backQueue.removeLast()
                }
                is State.Fail -> {
                    Toast.makeText(requireContext(), state.error.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
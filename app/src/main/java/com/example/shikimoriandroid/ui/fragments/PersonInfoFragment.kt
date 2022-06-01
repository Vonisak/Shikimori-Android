package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.FragmentCharacterInfoBinding
import com.example.shikimoriandroid.databinding.FragmentPersonInfoBinding
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.presentation.viewModels.PersonViewModel
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonInfoFragment(private val personId: Int) : Fragment() {

    private var _binding: FragmentPersonInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonInfoBinding.inflate(inflater, container, false)

        viewModel.getPerson(personId)
        observeModel()

        return binding.root
    }

    private fun observeModel() {
        viewModel.personState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Pending -> {
                }
                is State.Success -> {
                    (activity as MainActivity).supportActionBar?.title = state.data.nameEng
                    binding.info.headline.title.text = getString(R.string.main_info_headline_title)
                    binding.roles.headline.title.text =
                        getString(R.string.person_roles_headline_title)

                    val glide = GlideAdapter(this)
                    glide.loadImage(
                        "https://shikimori.one/${state.data.image.original}",
                        binding.image.poster
                    )

                    binding.info.nameRus.text = state.data.nameRus
                    binding.info.nameJap.text = state.data.nameJap
                    binding.info.birthDate.text = state.data.birthDate
                    binding.info.job.text = state.data.jobTitle

                    state.data.roles.forEach { role ->
                        val roleStr = "${role[0]}: ${role[1]}"
                        val roleTextView = TextView(requireContext())
                        roleTextView.text = roleStr
                        binding.roles.rolesContainer.addView(roleTextView)
                    }
                }
                is State.Fail -> {
                    Log.i("TAG", "error: ${state.error}")
                }
            }
        }
    }

}
package com.example.shikimoriandroid.ui.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.FragmentCharacterInfoBinding
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.presentation.viewModels.CharacterViewModel
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.ui.utils.collapse
import com.example.shikimoriandroid.ui.utils.expandWidth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterInfoFragment(private val characterId: Int) : Fragment() {

    private var _binding: FragmentCharacterInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharacterViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterInfoBinding.inflate(inflater, container, false)

        viewModel.getCharacter(characterId)
        observeModel()
        initListeners()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun observeModel() {
        viewModel.characterState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Pending -> {
                }
                is State.Success -> {
                    (activity as MainActivity).supportActionBar?.title = state.data.nameEng
                    binding.names.headline.title.text =
                        getString(R.string.character_names_headline_title)
                    binding.description.headline.title.text =
                        getString(R.string.description_headline_title)

                    val glide = GlideAdapter(this)
                    glide.loadImage(
                        "https://shikimori.one/${state.data.image.original}",
                        binding.image.poster
                    )

                    binding.names.nameRus.text = state.data.nameRus
                    binding.names.nameJap.text = state.data.nameJap
                    binding.names.alterNames.text = state.data.alterName

                    binding.description.description.text =
                        Html.fromHtml(state.data.descriptionHtml, Html.FROM_HTML_MODE_LEGACY)
                }
                is State.Fail -> {
                    Log.i("TAG", "Fail: ${state.error}")
                }
            }
        }
    }

    private fun initListeners() {
        binding.description.moreBtn.setOnClickListener {
            val moreBtn = it as ImageButton
            val viewHeight = resources.getDimensionPixelSize(R.dimen.anime_description_height)
            val descView = binding.description.description

            if (descView.height > viewHeight) {
                descView.collapse(viewHeight, binding.root)
                moreBtn.setImageResource(R.drawable.outline_expand_more_24)
            } else {
                descView.expandWidth(binding.root)
                moreBtn.setImageResource(R.drawable.outline_expand_less_24)
            }
        }
    }
}
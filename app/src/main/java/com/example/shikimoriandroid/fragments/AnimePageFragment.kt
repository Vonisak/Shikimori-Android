package com.example.shikimoriandroid.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shikimoriandroid.MainActivity
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.State
import com.example.shikimoriandroid.adapters.GlideAdapter
import com.example.shikimoriandroid.databinding.FragmentAnimePageBinding
import com.example.shikimoriandroid.viewModels.AnimePageViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class AnimePageFragment : Fragment() {

    private var _binding: FragmentAnimePageBinding? = null
    private val binding get() = _binding!!
    private val animePageViewModel by viewModels<AnimePageViewModel>()
    private val glide = GlideAdapter(this)
    private lateinit var title: String
    private val userListOpt = listOf(
        "Просмотрено",
        "Брошено",
        "Отложено",
        "Запланировано",
        "Пересматриваю",
        "Смотрю")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimePageBinding.inflate(inflater, container, false)

        val id = arguments?.getInt("amount")
        if (id != null) {
            animePageViewModel.getAnime(id)
        }

        binding.userListSpinner.setItems(userListOpt)

        observeModel()
        onClickListeners()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is MainActivity) {
            (activity as MainActivity).showBottomNav(false)
            title = (activity as MainActivity).supportActionBar?.title as String
        }
    }

    private fun observeModel() {
        animePageViewModel.getState().observe(viewLifecycleOwner) { it ->
            when (it) {
                is State.Pending -> {
                    //binding.swipeRefresh.isRefreshing = true
                }
                is State.Fail -> {
                    //binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(requireContext(), "Fail: ${it.error}", Toast.LENGTH_SHORT).show()
                }
                is State.Success -> {
                    Log.i("TAG", it.data.toString())
                    (activity as MainActivity).supportActionBar?.title = ""
                    when (it.data.status) {
                        "ongoing" -> {
                            binding.animeBasicInfo.episodes.visibility = View.GONE
                            binding.animeBasicInfo.animePageEpisodesAired.text = it.data.episodesAired
                            binding.animeBasicInfo.animePageEpisodesTotal.text = it.data.episodes
                            binding.animeBasicInfo.status.text =
                                resources.getString(R.string.anime_status_ongoing)
                        }
                        "released" -> {
                            binding.animeBasicInfo.episodesAired.visibility = View.GONE
                            binding.animeBasicInfo.animePageEpisodes.text = it.data.episodes
                            binding.animeBasicInfo.status.text =
                                resources.getString(R.string.anime_status_released)
                        }
                        "anons" -> {
                            binding.animeBasicInfo.episodes.visibility = View.GONE
                            binding.animeBasicInfo.episodesAired.visibility = View.GONE
                            binding.animeBasicInfo.animePageDuration.visibility = View.GONE
                            binding.animeBasicInfo.status.text =
                                resources.getString(R.string.anime_status_anons)
                        }
                    }
                    binding.animeBasicInfo.rating.text = when(it.data.ageRating) {
                        "g" -> "G"
                        "pg" -> "PG"
                        "pg_13" -> "PG-13"
                        "r" -> "R"
                        "r_plus" -> "R+"
                        "rx" -> "Rx"
                        else -> "?"
                    }
                    binding.animeBasicInfo.duration.text = it.data.duration
                    binding.animeBasicInfo.animePageEpisodes.text = it.data.episodes
                    binding.animeBasicInfo.animePageNameRus.text = it.data.nameRus
                    binding.animeBasicInfo.animePageType.text = when(it.data.kind) {
                        "tv" -> "TV сериал"
                        "movie" -> "Фильм"
                        "ova" -> "OVA"
                        "ona" -> "ONA"
                        "music" -> "Клип"
                        "special" -> "Спешл"
                        else -> "?"
                    }
                    glide.loadImage("https://shikimori.one/${it.data.poster.preview}", binding.header.poster)
                    glide.loadImage("https://shikimori.one/${it.data.poster.original}", binding.header.posterBack)
                    binding.descriptionLayout.description.text = it.data.description

                    if (binding.animeGenres.genresChipGroup.isEmpty()) {
                        it.data.genres.forEach {
                            binding.animeGenres.genresChipGroup.addChip(
                                requireContext(),
                                it.nameRus,
                                it.id
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onClickListeners() {
        binding.descriptionLayout.moreBtn.setOnClickListener {
            if (binding.descriptionLayout.description.maxLines == 3) {
                binding.descriptionLayout.description.maxLines = Integer.MAX_VALUE
                binding.descriptionLayout.moreBtn.setImageResource(R.drawable.outline_expand_less_24)
            } else {
                binding.descriptionLayout.description.maxLines = 3
                binding.descriptionLayout.moreBtn.setImageResource(R.drawable.outline_expand_more_24)
            }
        }

        binding.userListSpinner.setOnItemSelectedListener { view, position, id, item ->
            Toast.makeText(requireContext(), item.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun ChipGroup.addChip(context: Context, label: String, genreId: Int){
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = false
            isFocusable = true
            setOnClickListener {
                Log.i("TAG", label)
                val bundle = bundleOf("genre" to genreId)
                findNavController()
                    .navigate(R.id.action_animePageFragment_to_mainAnimeListFragment, bundle)
            }
            addView(this)
        }
    }

    override fun onDestroy() {
        if (activity is MainActivity) {
            (activity as MainActivity).showBottomNav(true)
            (activity as MainActivity).supportActionBar?.title = title
        }
        _binding = null
        super.onDestroy()
    }

}
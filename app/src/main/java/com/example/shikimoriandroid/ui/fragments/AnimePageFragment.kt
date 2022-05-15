package com.example.shikimoriandroid.ui.fragments

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
import com.example.shikimoriandroid.domain.utils.AnimeStringSwitcher
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.domain.entity.State
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.databinding.FragmentAnimePageBinding
import com.example.shikimoriandroid.data.model.anime.UserRate
import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.presentation.viewModels.AnimePageViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception

class AnimePageFragment : Fragment() {

    private var _binding: FragmentAnimePageBinding? = null
    private val binding get() = _binding!!
    private val animePageViewModel: AnimePageViewModel by viewModels()
    private val glide = GlideAdapter(this)
    private lateinit var title: String
    private var aId = 0
    private val userListOpt = listOf(
        "Просмотрено",
        "Брошено",
        "Отложено",
        "Запланировано",
        "Смотрю")
    private val userListOptEng = listOf(
        "completed",
        "dropped",
        "on_hold",
        "planned",
        "watching")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimePageBinding.inflate(inflater, container, false)

        aId = arguments?.getInt("amount")!!
        if((activity as MainActivity).users.isEmpty()) {
            animePageViewModel.getAnime(aId, "")
        } else {
            (activity as MainActivity).users[0].accessToken.let {
                if (it != null) {
                    animePageViewModel.getAnime(aId, it)
                    binding.userRateActionButton.visibility = View.VISIBLE
                }
            }
        }

        binding.userRate.statusSpinner.setItems(userListOpt)

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
        animePageViewModel.getAnimeInfoState().observe(viewLifecycleOwner) { it ->
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
                    val switcher = AnimeStringSwitcher()
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
                    binding.animeBasicInfo.rating.text = switcher.ageRatingSwitch(it.data.ageRating)
                    binding.animeBasicInfo.duration.text = it.data.duration
                    binding.animeBasicInfo.animePageEpisodes.text = it.data.episodes
                    binding.animeBasicInfo.animePageNameRus.text = it.data.nameRus
                    binding.animeBasicInfo.animePageType.text = switcher.kindSwitch(it.data.kind)
                    glide.loadImage("https://shikimori.one/${it.data.poster.preview}", binding.header.poster)
                    glide.loadImage("https://shikimori.one/${it.data.poster.original}", binding.header.posterBack)
                    binding.descriptionLayout.description.text = it.data.description

                    if (it.data.userRate != null)
                        binding.userRateActionButton.text = resources.getString(R.string.edit_in_my_list)
                    binding.userRate.textInputLayout.suffixText = "/${it.data.episodes}"
                    binding.userRate.statusSpinner.selectedIndex = when(it.data.userRate?.status) {
                        "planned" -> 3
                        "watching" -> 4
                        "on_hold" -> 2
                        "completed" -> 0
                        "dropped" -> 1
                        else -> 3
                    }
                    binding.userRate.ratingBar.rating = it.data.userRate?.score?.toFloat()?.div(2)
                        ?: 0f
                    binding.userRate.ratingNum.text =
                        if(it.data.userRate?.score == 0 || it.data.userRate == null) ""
                        else it.data.userRate?.score.toString()

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

        animePageViewModel.getUserRateState().observe(viewLifecycleOwner) {
            when (it) {
                is State.Pending -> {}
                is State.Fail -> {
                    Toast.makeText(activity, it.error?.message, Toast.LENGTH_SHORT).show()
                }
                is State.Success -> {
                    Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
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

        binding.userRate.statusSpinner.setOnItemSelectedListener { view, position, id, item ->
            Toast.makeText(requireContext(), item.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.userRate.ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            binding.userRate.ratingNum.text = (fl*2).toInt().toString()
        }

        binding.userRateActionButton.setOnClickListener {
            binding.userRateActionButton.visibility = View.GONE
            binding.userRate.root.visibility = View.VISIBLE
        }

        binding.userRate.saveButton.setOnClickListener {
            binding.userRate.root.visibility = View.GONE
            binding.userRateActionButton.visibility = View.VISIBLE

            val episodes = try {
                binding.userRate.episodesInput.text.toString().toInt()
            } catch (e: Exception) {
                null
            }
            val userRate = UserRate(
                userId = (activity as MainActivity).users[0].uid,
                targetId = aId,
                targetType = "Anime",
                score = (binding.userRate.ratingBar.rating*2).toInt(),
                status = userListOptEng[binding.userRate.statusSpinner.selectedIndex],
                episodes = episodes,
                id = 0
            )
            val userRates = UserRates(userRate)
            (activity as MainActivity).users[0].let {
                it.accessToken?.let { it1 ->
                    animePageViewModel.postUserRate(it1, userRates)
                }
            }
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
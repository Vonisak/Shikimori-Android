package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shikimoriandroid.domain.utils.AnimeStringSwitcher
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.anime.Stats
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.databinding.FragmentAnimePageBinding
import com.example.shikimoriandroid.data.model.anime.UserRate
import com.example.shikimoriandroid.data.model.anime.UserRates
import com.example.shikimoriandroid.presentation.viewModels.AnimePageViewModel
import com.example.shikimoriandroid.ui.adapters.CharacterAdapter
import com.example.shikimoriandroid.ui.adapters.PersonAdapter
import com.example.shikimoriandroid.ui.navigation.Screens
import com.example.shikimoriandroid.ui.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import kotlin.math.roundToInt

@AndroidEntryPoint
class AnimePageFragment(private val animeId: Int) : Fragment() {

    private var _binding: FragmentAnimePageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnimePageViewModel by viewModels()
    private val glide = GlideAdapter(this)
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var personAdapter: PersonAdapter
    private var isCreate = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimePageBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.checkUserAuth()
        if (isCreate) {
            isCreate = false
            viewModel.getAnime(animeId)
            viewModel.getRoles(animeId)
        }


        val userStatuses = resources.getStringArray(R.array.user_anime_statuses).toList()
        binding.userRate.statusSpinner.setItems(userStatuses)

        initCharacterRecycler()
        initPersonRecycler()
        observeModel()
        onClickListeners()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showBottomNav(false)
    }

    private fun observeModel() {
        viewModel.animeInfoState.observe(viewLifecycleOwner) { it ->
            when (it) {
                is State.Pending -> {
                    //binding.swipeRefresh.isRefreshing = true
                }
                is State.Fail -> {
                    //binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(requireContext(), "Fail: ${it.error}", Toast.LENGTH_SHORT).show()
                }
                is State.Success -> {
                    //Log.i("TAG", it.data.toString())
                    (activity as MainActivity).supportActionBar?.title = it.data.nameEng
                    val switcher = AnimeStringSwitcher()
                    Log.i("TAG", it.data.ratesStats.toString())
                    when (it.data.status) {
                        "ongoing" -> {
                            binding.animeBasicInfo.episodes.visibility = View.GONE
                            binding.animeBasicInfo.animePageEpisodesAired.text =
                                it.data.episodesAired
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
                    glide.loadImage(
                        "https://shikimori.one/${it.data.poster.preview}",
                        binding.header.poster
                    )
                    glide.loadImage(
                        "https://shikimori.one/${it.data.poster.original}",
                        binding.header.posterBack
                    )
                    binding.descriptionLayout.description.text = it.data.description

                    if (it.data.userRate != null)
                        binding.userRateActionButton.text =
                            resources.getString(R.string.edit_in_my_list)
                    binding.userRate.textInputLayout.suffixText = "/${it.data.episodes}"
                    binding.userRate.statusSpinner.selectedIndex = when (it.data.userRate?.status) {
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
                        if (it.data.userRate?.score == 0 || it.data.userRate == null) ""
                        else it.data.userRate?.score.toString()

                    if (binding.animeGenres.genresChipGroup.isEmpty()) {
                        it.data.genres.forEach {
                            binding.animeGenres.genresChipGroup.addChip(
                                requireContext(),
                                it.nameRus
                            ) {
                                viewModel.navigateTo(Screens.mainList(it.id.toString()))
                            }
                        }
                    }
                    binding.animeBasicInfo.headline.title.text =
                        getString(R.string.main_info_headline_title)
                    binding.descriptionLayout.headline.title.text =
                        getString(R.string.description_headline_title)
                    binding.animeGlobalRating.headline.title.text =
                        getString(R.string.global_rating_headline_title)
                    setGlobalRating(it.data.score)

                    binding.characters.headline.title.text =
                        getString(R.string.main_characters_headline_title)
                    binding.persons.headline.title.text = getString(R.string.authors_headline_title)
                    binding.usersScores.headline.title.text =
                        getString(R.string.users_rates_headline_title)
                    binding.usersStatuses.headline.title.text =
                        getString(R.string.users_statuses_headline_title)

                    setUsersScores(it.data.ratesStats)
                    setUsersStatuses(it.data.statusesStats)
                }
            }
        }

        viewModel.postUserRateState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Pending -> {
                }
                is State.Fail -> {
                    Toast.makeText(activity, it.error.toString(), Toast.LENGTH_SHORT).show()
                }
                is State.Success -> {
                    Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.userAuth.observe(viewLifecycleOwner) { auth ->
            if (auth) {
                binding.userRateActionButton.visibility = View.VISIBLE
            } else {
                binding.userRateActionButton.visibility = View.GONE
            }
        }

        viewModel.rolesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Pending -> {
                }
                is State.Success -> {
                    binding.characters.headline.link.isVisible = true
                    binding.persons.headline.link.isVisible = true

                    val characters = state.data.filter { it.characterPreview != null }
                    Log.i("TAG", "characters size: ${characters.size}")
                    val persons = state.data.filter { it.personPreview != null }

                    val personFilterList =
                        resources.getStringArray(R.array.anime_page_person_filter).toList()
                    val characterFilterList =
                        resources.getStringArray(R.array.anime_page_character_filter).toList()
                    val personsFiltered =
                        persons.filter { it.rolesRus.roleFilter(personFilterList) }
                    val charactersFiltered =
                        characters.filter { it.rolesRus.roleFilter(characterFilterList) }

                    val charactersList = charactersFiltered.map { it.characterPreview!! }

                    personAdapter.roles = personsFiltered
                    characterAdapter.characters = charactersList

                    binding.persons.headline.root.setOnClickListener {
                        viewModel.navigateTo(Screens.personList(persons))
                    }
                    binding.characters.headline.root.setOnClickListener {
                        viewModel.navigateTo(Screens.characterList(characters))
                    }
                }
                is State.Fail -> {
                }
            }
        }
    }

    private fun setGlobalRating(rating: String) {
        val ratingNotices = resources.getStringArray(R.array.rating_notice_array)
        val ratingNotice = ratingNotices[rating.toFloat().toInt() - 1]
        binding.animeGlobalRating.ratingNotice.text = ratingNotice
        binding.animeGlobalRating.ratingText.text = rating
        binding.animeGlobalRating.globalRatingBar.progress = rating.toFloat().roundToInt()
    }

    private fun onClickListeners() {
        binding.descriptionLayout.moreBtn.setOnClickListener {
            val moreBtn = it as ImageButton
            val viewHeight = resources.getDimensionPixelSize(R.dimen.anime_description_height)
            val descView = binding.descriptionLayout.description

            if (descView.height > viewHeight) {
                descView.collapse(viewHeight, binding.root)
                moreBtn.setImageResource(R.drawable.outline_expand_more_24)
            } else {
                descView.expand(binding.root)
                moreBtn.setImageResource(R.drawable.outline_expand_less_24)
            }
        }

        binding.userRate.statusSpinner.setOnItemSelectedListener { _, _, _, item ->
            Toast.makeText(requireContext(), item.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.userRate.ratingBar.setOnRatingBarChangeListener { _, fl, _ ->
            binding.userRate.ratingNum.text = (fl * 2).toInt().toString()
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

            val userStatusesJson =
                resources.getStringArray(R.array.user_anime_statuses_json).toList()

            val userRate = UserRate(
                userId = 0,
                targetId = animeId,
                targetType = "Anime",
                score = (binding.userRate.ratingBar.rating * 2).toInt(),
                status = userStatusesJson[binding.userRate.statusSpinner.selectedIndex],
                episodes = episodes,
                id = 0
            )
            val userRates = UserRates(userRate)
            viewModel.postUserRate(userRates)
        }
    }

    private fun initPersonRecycler() {
        val layoutManager = LinearLayoutManager(requireContext())
        personAdapter = PersonAdapter(glide) { personId ->
            viewModel.navigateTo(Screens.personInfo(personId))
        }
        binding.persons.recycler.layoutManager = layoutManager
        binding.persons.recycler.adapter = personAdapter
    }

    private fun initCharacterRecycler() {
        val layoutManager = GridLayoutManager(context, 3)
        characterAdapter = CharacterAdapter(glide) { characterId ->
            viewModel.navigateTo(Screens.characterInfo(characterId))
        }
        binding.characters.recycler.layoutManager = layoutManager
        binding.characters.recycler.adapter = characterAdapter
    }

    private fun setUsersScores(scores: List<Stats>) {
        val maxScore = scores.maxOf { it.value }
        val maxWidth = resources.getDimensionPixelSize(R.dimen.gist_max_width_scores)
        scores.forEach {
            binding.usersScores.gistContainer.addViewWithLayout(
                it,
                requireContext(),
                maxScore,
                maxWidth
            )
        }
    }

    private fun setUsersStatuses(statuses: List<Stats>) {
        val maxStatus = statuses.maxOf { it.value }
        val maxWidth = resources.getDimensionPixelSize(R.dimen.gist_max_width_statuses)
        statuses.forEach {
            binding.usersStatuses.gistContainer.addViewWithLayout(
                it,
                requireContext(),
                maxStatus,
                maxWidth
            )
        }
    }

    override fun onDestroy() {
        (activity as MainActivity).showBottomNav(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        _binding = null
        super.onDestroy()
    }

}
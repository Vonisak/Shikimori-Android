package com.example.shikimoriandroid.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shikimoriandroid.domain.utils.AnimeStringSwitcher
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.anime.*
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.databinding.FragmentAnimePageBinding
import com.example.shikimoriandroid.domain.utils.Constants
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
            viewModel.getExternalLinks(animeId)
        }

        val userStatuses = resources.getStringArray(R.array.user_anime_statuses).toList()
        binding.userRate.statusSpinner.setItems(userStatuses)

        setHeadLines()
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
                            binding.animeBasicInfo.episodes.hide()
                            binding.animeBasicInfo.animePageEpisodesAired.text =
                                it.data.episodesAired
                            binding.animeBasicInfo.animePageEpisodesTotal.text = it.data.episodes
                            binding.animeBasicInfo.status.text =
                                resources.getString(R.string.anime_status_ongoing)
                        }
                        "released" -> {
                            binding.animeBasicInfo.episodesAired.hide()
                            binding.animeBasicInfo.animePageEpisodes.text = it.data.episodes
                            binding.animeBasicInfo.status.text =
                                resources.getString(R.string.anime_status_released)
                        }
                        "anons" -> {
                            binding.animeBasicInfo.episodes.hide()
                            binding.animeBasicInfo.episodesAired.hide()
                            binding.animeBasicInfo.animePageDuration.hide()
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
                    binding.userRate.userRating.ratingBar.rating =
                        it.data.userRate?.score?.toFloat()?.div(2)
                            ?: 0f
                    binding.userRate.userRating.ratingText.text =
                        if (it.data.userRate?.score == 0 || it.data.userRate == null) ""
                        else it.data.userRate?.score.toString()

                    val userRate = it.data.userRate
                    if (userRate != null && userRate.score != 0) {
                        setRatingNotice(
                            userRate.score.toFloat(),
                            binding.userRate.userRating.ratingNotice
                        )
                    }

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
                    setGlobalRating(it.data.score)
                    setUsersScores(it.data.ratesStats)
                    setUsersStatuses(it.data.statusesStats)
                    setDescription(it.data.description)
                    setScreenshots(it.data.screenshots)
                }
            }
        }

        viewModel.postUserRateState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Pending -> {
                }
                is State.Fail -> {
                    this.toastShort(it.error.toString())
                }
                is State.Success -> {
                    this.toastShort(getString(R.string.success_post))
                }
            }
        }

        viewModel.userAuth.observe(viewLifecycleOwner) { auth ->
            if (auth) {
                binding.userRateActionButton.show()
            } else {
                binding.userRateActionButton.hide()
            }
        }

        viewModel.rolesState.observe(viewLifecycleOwner) { roles ->
            when (roles) {
                is State.Pending -> {
                }
                is State.Success -> {

                    val characters = roles.data.filter { it.characterPreview != null }
                    val persons = roles.data.filter { it.personPreview != null }

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

        viewModel.externalLinksState.observe(viewLifecycleOwner) { externalLinks ->
            when (externalLinks) {
                is State.Pending -> {
                }
                is State.Success -> {
                    setExternalLinks(externalLinks.data)
                }
                is State.Fail -> {
                }
            }
        }
    }

    private fun setScreenshots(screenshots: List<Screenshot>) {
        if (screenshots.isEmpty()) {
            binding.screenshots.root.hide()
        } else {
            glide.loadImage(
                "https://shikimori.one/${screenshots[0].preview}",
                binding.screenshots.screenshot1.screenshot
            )
            glide.loadImage(
                "https://shikimori.one/${screenshots[1].preview}",
                binding.screenshots.screenshot2.screenshot
            )
            binding.screenshots.screenshot1.root.setOnClickListener {
                binding.screenshots.screenshot1.screenshot.openImageViewer(this, screenshots, 0)
            }
            binding.screenshots.screenshot2.root.setOnClickListener {
                binding.screenshots.screenshot2.screenshot.openImageViewer(this, screenshots, 1)
            }

        }
    }

    private fun setDescription(description: String?) {
        if (description == null) {
            binding.descriptionLayout.root.hide()
        } else {
            binding.descriptionLayout.description.text = description
        }
    }

    private fun setGlobalRating(rating: String) {
        if (rating == "0.0") {
            binding.animeGlobalRating.root.hide()
            return
        }
        binding.animeGlobalRating.globalRating.ratingBar.setIsIndicator(true)
        setRatingNotice(rating.toFloat(), binding.animeGlobalRating.globalRating.ratingNotice)
        binding.animeGlobalRating.globalRating.ratingText.text = rating
        binding.animeGlobalRating.globalRating.ratingBar.progress = rating.toFloat().roundToInt()
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

        binding.userRate.userRating.ratingBar.setOnRatingBarChangeListener { _, fl, _ ->
            if (fl != 0F) {
                setRatingNotice(fl * 2, binding.userRate.userRating.ratingNotice)
            }
            binding.userRate.userRating.ratingText.text = (fl * 2).toInt().toString()
        }

        binding.screenshots.headline.root.setOnClickListener {
            viewModel.navigateTo(Screens.screenshots(animeId))
        }

        binding.userRateActionButton.setOnClickListener {
            binding.userRateActionButton.hide()
            binding.userRate.root.expand(binding.root)
        }

        binding.userRate.saveButton.setOnClickListener {
            binding.userRate.root.collapse(0, binding.root)
            binding.userRateActionButton.show()

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
                score = (binding.userRate.userRating.ratingBar.rating * 2).toInt(),
                status = userStatusesJson[binding.userRate.statusSpinner.selectedIndex],
                episodes = episodes,
                id = 0
            )
            val userRates = UserRates(userRate)
            viewModel.postUserRate(userRates)
        }
    }

    private fun setRatingNotice(rating: Float, textView: TextView) {
        val ratingNotices = resources.getStringArray(R.array.rating_notice_array)
        val ratingNotice = ratingNotices[rating.toInt() - 1]
        textView.text = ratingNotice
    }

    private fun setHeadLines() {
        binding.characters.headline.link.show()
        binding.persons.headline.link.show()
        binding.screenshots.headline.link.show()
        binding.characters.headline.title.text =
            getString(R.string.main_characters_headline_title)
        binding.persons.headline.title.text = getString(R.string.authors_headline_title)
        binding.usersScores.headline.title.text =
            getString(R.string.users_rates_headline_title)
        binding.usersStatuses.headline.title.text =
            getString(R.string.users_statuses_headline_title)
        binding.animeBasicInfo.headline.title.text =
            getString(R.string.main_info_headline_title)
        binding.descriptionLayout.headline.title.text =
            getString(R.string.description_headline_title)
        binding.animeGlobalRating.headline.title.text =
            getString(R.string.global_rating_headline_title)
        binding.screenshots.headline.title.text = getString(R.string.screenshots_headline_title)
        binding.externalLinks.headline.title.text = getString(R.string.other_sources_headline_title)
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
            binding.usersScores.gistContainer.addViewWithLayout(it, maxScore, maxWidth)
        }
    }

    private fun setUsersStatuses(statuses: List<Stats>) {
        val maxStatus = statuses.maxOf { it.value }
        val maxWidth = resources.getDimensionPixelSize(R.dimen.gist_max_width_statuses)
        statuses.forEach {
            binding.usersStatuses.gistContainer.addViewWithLayout(it, maxStatus, maxWidth)
        }
    }

    private fun setExternalLinks(externalLinks: List<ExternalLink>) {
        if (externalLinks.isNotEmpty()) {
            externalLinks.forEach { externalLink ->
                binding.externalLinks.externalLinksContainer.addLink(externalLink) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(externalLink.url)
                    startActivity(intent)
                }
            }
        } else {
            binding.externalLinks.root.hide()
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
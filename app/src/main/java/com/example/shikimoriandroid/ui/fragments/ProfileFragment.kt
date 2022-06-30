package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.fragment.app.viewModels
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.anime.Stats
import com.example.shikimoriandroid.data.model.user.ActivityItem
import com.example.shikimoriandroid.databinding.FragmentProfileBinding
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.presentation.viewModels.ProfileViewModel
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.ui.adapters.HistoryAdapter
import com.example.shikimoriandroid.ui.navigation.Screens
import com.example.shikimoriandroid.ui.utils.addViewOnUserActivity
import com.example.shikimoriandroid.ui.utils.addViewWithLayout
import com.example.shikimoriandroid.ui.utils.show
import com.example.shikimoriandroid.ui.utils.toastShort
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseBottomNavFragment() {

    companion object {

        const val TITLE = "Профиль"
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val glideAdapter = GlideAdapter(this)
    private val viewModel: ProfileViewModel by viewModels()
    private var isCreate = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar?.title = TITLE

        if (isCreate) {
            isCreate = false
            viewModel.getProfileInfo()
            viewModel.getUserHistory(2, 1)
        }

        setHeadlines()
        observeModel()
        listeners()

        return binding.root
    }

    private fun listeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getProfileInfo()
        }
        binding.userList.setOnClickListener {
            viewModel.navigateTo(Screens.userList())
        }
        binding.userHistory.headline.root.setOnClickListener {
            viewModel.navigateTo(Screens.userHistory())
        }
    }

    private fun observeModel() {
        viewModel.userProfileState.observe(viewLifecycleOwner) { profileInfo ->
            when (profileInfo) {
                is State.Pending -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is State.Fail -> {
                    binding.swipeRefresh.isRefreshing = false
                    this.toastShort("Fail: ${profileInfo.error}")
                }
                is State.Success -> {
                    binding.profileHeader.nickname.text = profileInfo.data.nickname

                    glideAdapter.loadImage(
                        profileInfo.data.images.imageX148,
                        binding.profileHeader.avatar
                    )

                    val userAnimeStatuses = profileInfo.data.stats.statuses.anime
                    val statuses = resources.getStringArray(R.array.user_profile_anime_statuses)
                    val stats = userAnimeStatuses.mapIndexed { i, animeStatus ->
                        Stats(statuses[i], animeStatus.size)
                    }

                    setUserStats(stats)
                    setUserActivity(profileInfo.data.stats.activity)

                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        viewModel.historyState.observe(viewLifecycleOwner) { history ->
            when (history) {
                is State.Pending -> {
                }
                is State.Success -> {
                    Log.i("TAG", "history: ${history.data}")
                    val adapter = HistoryAdapter(glideAdapter) { animeId ->
                        viewModel.navigateTo(Screens.animePage(animeId))
                    }
                    adapter.historyList = history.data
                    binding.userHistory.history.recycler.adapter = adapter
                }
                is State.Fail -> {
                    Log.i("TAG", "error: ${history.error}")
                }
            }
        }
    }

    private fun setUserStats(statuses: List<Stats>) {
        if (binding.userAnimeStats.gistContainer.isEmpty()) {
            val maxStatus = statuses.maxOf { it.value }
            val maxWidth = resources.getDimensionPixelSize(R.dimen.gist_max_width_statuses)
            statuses.forEach {
                binding.userAnimeStats.gistContainer.addViewWithLayout(it, maxStatus, maxWidth)
            }
        }
    }

    private fun setUserActivity(activity: List<ActivityItem>) {
        if (binding.userActivity.activityContainer.isNotEmpty()) return
        val maxHeight = resources.getDimensionPixelSize(R.dimen.gist_max_height_activity)
        val maxValue = activity.maxOf { it.value }
        activity.forEach { activityItem ->
            binding.userActivity.activityContainer.addViewOnUserActivity(
                activityItem,
                maxValue,
                maxHeight
            )
        }
    }

    private fun setHeadlines() {
        binding.userHistory.headline.link.show()
        binding.userAnimeStats.headline.title.text =
            getString(R.string.user_anime_statuses_headline_title)
        binding.userActivity.headline.title.text = getString(R.string.user_activity_headline_title)
        binding.userHistory.headline.title.text = getString(R.string.user_history_headline_title)
    }
}
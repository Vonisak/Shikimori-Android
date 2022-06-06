package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.ui.adapters.UserListAdapter
import com.example.shikimoriandroid.databinding.FragmentUserListRecyclerBinding
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.presentation.viewModels.UserAnimeListViewModel
import com.example.shikimoriandroid.ui.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListRecyclerFragment(val position: Int) : Fragment() {

    private var _binding: FragmentUserListRecyclerBinding? = null
    private val binding get() = _binding!!
    private val userList = mutableListOf<AnimeRates>()
    private val userListViewModel: UserAnimeListViewModel by viewModels()
    private lateinit var status: String
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisiblesItems = 0
    private var page = 1
    private var loading = true
    private var isCreate = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListRecyclerBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        status = when (position) {
            0 -> "watching"
            1 -> "completed"
            2 -> "planned"
            3 -> "on_hold"
            else -> "dropped"
        }

        if (isCreate) {
            animeListRequest()
            isCreate = false
        }
        observeModel()
        binding.userListRecycler.adapter = UserListAdapter(userList) { animeId ->
            userListViewModel.navigateTo(Screens.animePage(animeId))
        }

        binding.userListRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount =
                        (binding.userListRecycler.layoutManager as LinearLayoutManager).childCount
                    totalItemCount =
                        (binding.userListRecycler.layoutManager as LinearLayoutManager).itemCount
                    pastVisiblesItems =
                        (binding.userListRecycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems + 4 >= totalItemCount) {
                            loading = false
                            page++
                            animeListRequest()
                        }
                    }
                }
            }
        })

        return binding.root
    }

    private fun observeModel() {
        userListViewModel.userListState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Pending -> {
                    //binding.swipeRefresh.isRefreshing = true
                }
                is State.Fail -> {
                    //binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(requireContext(), "Fail: ${it.error}", Toast.LENGTH_SHORT).show()
                }
                is State.Success -> {
                    if (!userList.containsAll(it.data)) {
                        userList.addAll(it.data)
                        //Log.i("TAG", it.data.toString())
                        binding.userListRecycler.adapter?.notifyDataSetChanged()
                        loading = true
                    }
                }
            }
        }
    }

    private fun animeListRequest() {
        userListViewModel.getUserAnimeRates(30, page, status)
    }

    override fun onDestroy() {
        super.onDestroy()
        isCreate = true
        _binding = null
    }
}
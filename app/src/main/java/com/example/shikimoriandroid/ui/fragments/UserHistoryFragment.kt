package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.data.model.user.History
import com.example.shikimoriandroid.databinding.FragmentUserHistoryBinding
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.presentation.viewModels.ProfileViewModel
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.ui.adapters.HistoryAdapter
import com.example.shikimoriandroid.ui.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserHistoryFragment : Fragment() {

    companion object {

        const val TITLE = "История"
    }

    private var _binding: FragmentUserHistoryBinding? = null
    private val binding get() = _binding!!
    private val glideAdapter = GlideAdapter(this)
    private val viewModel: ProfileViewModel by viewModels()
    private val adapter = HistoryAdapter(glideAdapter) { animeId ->
        viewModel.navigateTo(Screens.animePage(animeId))
    }
    private var page: Int = 1
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisiblesItems = 0
    private var loading = true
    private var historyList = mutableListOf<History>()

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomNav(false)
        (activity as MainActivity).supportActionBar?.title = TITLE
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserHistoryBinding.inflate(inflater, container, false)

        binding.recycler.adapter = adapter
        initRecycler()
        observeModel()

        viewModel.getUserHistory(15, page)

        return binding.root
    }

    private fun observeModel() {
        viewModel.historyState.observe(viewLifecycleOwner) { history ->
            when (history) {
                is State.Pending -> {}
                is State.Success -> {
                    historyList.addAll(history.data)
                    adapter.historyList = historyList
                    loading = true
                }
                is State.Fail -> {}
            }
        }
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.layoutManager = layoutManager

        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.childCount
                    //Log.i("TAG", "visibleItemCount=$visibleItemCount")
                    totalItemCount = layoutManager.itemCount
                    //Log.i("TAG", "totalItemCount=$totalItemCount")
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    //Log.i("TAG", "pastVisiblesItems=$pastVisiblesItems")
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems + 4 >= totalItemCount) {
                            loading = false
                            page++
                            viewModel.getUserHistory(15, page)
                            //Log.i("TAG", "page=$page")
                        }
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        (activity as MainActivity).showBottomNav(true)
    }

}
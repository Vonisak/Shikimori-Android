package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.ui.adapters.AnimeListAdapter
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.FragmentMainAnimeListBinding
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.domain.entity.State
import com.example.shikimoriandroid.presentation.viewModels.MainListViewModel

class MainAnimeListFragment :
    BaseBottomNavFragment(),
    SearchView.OnQueryTextListener,
    MainActivity.ItemReselectedListener,
    MainListBottomSheetFilterFragment.OnClickListener{

    private var _binding: FragmentMainAnimeListBinding? = null
    private val binding get() = _binding!!
    private val mainListViewModel: MainListViewModel by viewModels()
    private var animeList = mutableListOf<AnimeInfo>()
    private val glideAdapter = GlideAdapter(this)
    private val adapter: AnimeListAdapter = AnimeListAdapter(animeList, glideAdapter) { animeId ->
        Log.i("TAG", "click $animeId")
        val bundle = bundleOf("amount" to animeId)
        navigate(R.id.action_mainAnimeListFragment_to_animePageFragment, bundle)
    }
    private var page = 1
    private val limit = 20
    private var order = "ranked"
    private var genre = ""
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisiblesItems = 0
    private var loading = true
    private var isCreate = true
    private var searchStr = ""
    //private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private val modalBottomSheet = MainListBottomSheetFilterFragment(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainAnimeListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        genre = arguments?.getInt("genre").toString()
        if (genre == "null") genre = ""
        Log.i("TAG", genre)

        if (isCreate) {
            mainListViewModel.getAnimeList(limit, page, order, searchStr, genre)
            Log.i("TAG", animeList.toString())
            Log.i("TAG", "createView")
            isCreate = false
        }
        observeModel()
        recyclerSettings()
        swipeRefreshListener()
        (activity as MainActivity).setItemReselectedListener(this)

        //sheetBehavior = BottomSheetBehavior.from(binding.bottomSheetFilter.root)

        return binding.root
    }

    private fun swipeRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            page = 1
            animeList.clear()
            mainListViewModel.getAnimeList(limit, page, order, searchStr, genre)
        }
    }

    private fun recyclerSettings() {
        val layoutManager = GridLayoutManager(context, 2)
        binding.mainAnimeListRecycler.adapter = adapter
        binding.mainAnimeListRecycler.layoutManager = layoutManager

        binding.mainAnimeListRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                            mainListViewModel.getAnimeList(limit, page, order, searchStr, genre)
                            //Log.i("TAG", "page=$page")
                        }
                    }
                }
            }
        })
    }

    private fun observeModel() {
        Log.i("TAG", "observeModel")

        mainListViewModel.getState().observe(viewLifecycleOwner) { it ->
            when (it) {
                is State.Pending -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is State.Fail -> {
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(requireContext(), "Fail: ${it.error}", Toast.LENGTH_SHORT).show()
                }
                is State.Success -> {
                    if (!animeList.containsAll(it.data)) {
                        animeList.addAll(it.data)
                        adapter.update(animeList)
                        adapter.notifyDataSetChanged()
                        binding.swipeRefresh.isRefreshing = false
                        loading = true
                    }
                }
            }
        }
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_list_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_list_action_filter -> {
                //Toast.makeText(activity, "filter soon", Toast.LENGTH_SHORT).show()
                activity?.supportFragmentManager?.let {
                    modalBottomSheet.show(it, MainListBottomSheetFilterFragment.TAG)
                }
            }
            R.id.main_list_action_search -> {
                (item.actionView as SearchView).setOnQueryTextListener(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        Log.i("TAG", "textSubmit")
        searchStr = query
        page = 1
        animeList.clear()
        mainListViewModel.getAnimeList(limit, page, order, query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onDestroy() {
        Log.i("TAG", "destroy")
        isCreate = true
        _binding = null
        super.onDestroy()
    }

    override fun reselect() {
        Log.i("TAG", "reselect")
        searchStr = ""
        page = 1
        animeList.clear()
        mainListViewModel.getAnimeList(limit, page, order, searchStr)
    }

    override fun onClick() {
        Log.i("TAG", "click button")
    }
}
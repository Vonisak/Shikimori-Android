package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
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
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.presentation.viewModels.MainListViewModel
import com.example.shikimoriandroid.ui.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAnimeListFragment(private val genre: String = "") :
    BaseBottomNavFragment(),
    SearchView.OnQueryTextListener,
    MainActivity.ItemReselectedListener,
    MainListBottomSheetFilterFragment.OnClickListener{

    companion object {

        const val TITLE = "Список"
    }

    private var _binding: FragmentMainAnimeListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainListViewModel by viewModels()
    private var animeList = mutableListOf<AnimeInfo>()
    private val glideAdapter = GlideAdapter(this)
    private val adapter: AnimeListAdapter = AnimeListAdapter(animeList, glideAdapter) { animeId ->
        viewModel.navigateTo(Screens.animePage(animeId))
    }
    private var page = 1
    private val limit = 20
    private var order = "ranked"
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisiblesItems = 0
    private var loading = true
    private var isCreate = true
    private var searchStr = ""
    //private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private val modalBottomSheet = MainListBottomSheetFilterFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainAnimeListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = TITLE

        if (isCreate) {
            viewModel.getAnimeList(limit, page, order, searchStr, genre)
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
            viewModel.getAnimeList(limit, page, order, searchStr, genre)
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
                            viewModel.getAnimeList(limit, page, order, searchStr, genre)
                            //Log.i("TAG", "page=$page")
                        }
                    }
                }
            }
        })
    }

    private fun observeModel() {
        viewModel.mainAnimeListState.observe(viewLifecycleOwner) {
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
        searchStr = query
        page = 1
        animeList.clear()
        viewModel.getAnimeList(limit, page, order, query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onDestroy() {
        isCreate = true
        _binding = null
        super.onDestroy()
    }

    override fun reselect() {
        searchStr = ""
        page = 1
        animeList.clear()
        viewModel.getAnimeList(limit, page, order, searchStr)
    }

    override fun onClick() {
        Log.i("TAG", "click button")
    }
}
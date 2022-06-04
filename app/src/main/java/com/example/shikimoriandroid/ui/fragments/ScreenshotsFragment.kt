package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shikimoriandroid.databinding.FragmentScreenshotsBinding
import com.example.shikimoriandroid.presentation.entity.State
import com.example.shikimoriandroid.presentation.viewModels.ScreenshotsViewModel
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.ui.adapters.ScreenshotsAdapter
import com.example.shikimoriandroid.ui.utils.openImageViewer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenshotsFragment(private val animeId: Int) : Fragment() {

    companion object {

        const val TITLE = "Кадры"
    }

    private var _binding: FragmentScreenshotsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScreenshotsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = TITLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScreenshotsBinding.inflate(inflater, container, false)

        viewModel.getScreenshots(animeId)
        observeModel()

        return binding.root
    }

    private fun observeModel() {
        viewModel.screenshotsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Pending -> {}
                is State.Success -> {
                    val adapter = ScreenshotsAdapter(GlideAdapter((this))) { imageView, position ->
                        imageView.openImageViewer(this, state.data, position)
                    }
                    val layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.recycler.layoutManager = layoutManager
                    binding.recycler.adapter = adapter
                    adapter.screenshots = state.data
                }
                is State.Fail -> {
                    Toast.makeText(requireContext(), "error: ${state.error}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
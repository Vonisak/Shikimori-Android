package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shikimoriandroid.data.model.anime.Role
import com.example.shikimoriandroid.databinding.FragmentPersonListBinding
import com.example.shikimoriandroid.presentation.viewModels.NavigationModel
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.ui.adapters.PersonAdapter
import com.example.shikimoriandroid.ui.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonListFragment(private val persons: List<Role>) : Fragment() {

    companion object {

        const val TITLE = "Авторы"
    }

    private var _binding: FragmentPersonListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NavigationModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = TITLE
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonListBinding.inflate(inflater, container, false)

        initPersonRecycler()

        return binding.root
    }

    private fun initPersonRecycler() {
        val layoutManager = LinearLayoutManager(requireContext())
        val personAdapter = PersonAdapter(GlideAdapter(this)) { personId ->
            viewModel.navigateTo(Screens.personInfo(personId))
        }
        personAdapter.roles = persons
        binding.personRecycler.layoutManager = layoutManager
        binding.personRecycler.adapter = personAdapter
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
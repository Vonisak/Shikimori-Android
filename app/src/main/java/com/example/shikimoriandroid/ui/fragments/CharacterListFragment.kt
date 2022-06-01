package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shikimoriandroid.data.model.anime.Role
import com.example.shikimoriandroid.databinding.FragmentCharacterListBinding
import com.example.shikimoriandroid.presentation.viewModels.NavigationModel
import com.example.shikimoriandroid.ui.activity.MainActivity
import com.example.shikimoriandroid.ui.adapters.CharacterAdapter
import com.example.shikimoriandroid.ui.adapters.GlideAdapter
import com.example.shikimoriandroid.ui.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterListFragment(private val characters: List<Role>) : Fragment() {

    companion object {

        const val TITLE = "Герои"
    }

    private var _binding: FragmentCharacterListBinding? = null
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
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)

        initCharactersRecycler()

        return binding.root
    }

    private fun initCharactersRecycler() {
        val layoutManager = GridLayoutManager(context, 3)
        val characterAdapter = CharacterAdapter(GlideAdapter(this)) { characterId ->
            viewModel.navigateTo(Screens.characterInfo(characterId))
        }
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = characterAdapter
        characterAdapter.characters = characters.map { it.characterPreview!! }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}
package com.example.shikimoriandroid.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.data.model.anime.CharacterPreview
import com.example.shikimoriandroid.databinding.CharactersListItemBinding
import com.example.shikimoriandroid.ui.adapters.GlideAdapter

class CharacterItemViewHolder(val view: View, private val glideAdapter: GlideAdapter) :
    RecyclerView.ViewHolder(view) {
    val binding = CharactersListItemBinding.bind(view)

    fun bind(character: CharacterPreview, onCharacterClick: (personId: Int) -> Unit) = with(binding) {
        glideAdapter.loadImage("https://shikimori.one/${character.image.original}", image)
        name.text = character.nameRus
        name.setOnClickListener {
            onCharacterClick(character.id)
        }
        image.setOnClickListener {
            onCharacterClick(character.id)
        }
    }
}
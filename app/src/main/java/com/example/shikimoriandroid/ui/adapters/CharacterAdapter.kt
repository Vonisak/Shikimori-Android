package com.example.shikimoriandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.anime.CharacterPreview
import com.example.shikimoriandroid.ui.viewholder.CharacterItemViewHolder

class CharacterAdapter(
    private val glide: GlideAdapter,
    private val onCharacterClick: (personId: Int) -> Unit
) : RecyclerView.Adapter<CharacterItemViewHolder>() {

    var characters: List<CharacterPreview> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.characters_list_item, parent, false)
        return CharacterItemViewHolder(view, glide)
    }

    override fun onBindViewHolder(holder: CharacterItemViewHolder, position: Int) {
        holder.bind(characters[position], onCharacterClick)
    }

    override fun getItemCount(): Int = characters.size
}
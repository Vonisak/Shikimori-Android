package com.example.shikimoriandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.user.AnimeRates
import com.example.shikimoriandroid.ui.viewholder.AnimeItemProfileViewHolder

class UserListAdapter(
    private val values: MutableList<AnimeRates>,
    private val onAnimeClick: (animeId: Int) -> Unit
) : RecyclerView.Adapter<AnimeItemProfileViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeItemProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_item, parent, false)
        return AnimeItemProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeItemProfileViewHolder, position: Int) {
        holder.bind(values[position], onAnimeClick)
    }

    override fun getItemCount(): Int = values.size

}
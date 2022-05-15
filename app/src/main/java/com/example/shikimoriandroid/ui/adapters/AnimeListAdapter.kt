package com.example.shikimoriandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.anime.AnimeInfo
import com.example.shikimoriandroid.ui.viewholder.AnimeItemViewHolder

class AnimeListAdapter(
    private val values: MutableList<AnimeInfo>,
    private val glide: GlideAdapter,
    private val onAnimeClick: (animeId: Int) -> Unit
) : RecyclerView.Adapter<AnimeItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_item, parent, false)
        return AnimeItemViewHolder(view, glide)
    }

    override fun onBindViewHolder(holder: AnimeItemViewHolder, position: Int) {
        holder.bind(values[position], onAnimeClick)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    fun update(newList: List<AnimeInfo>) {
        val messageDiffUtilCallback =
            RoomDiffUtilCallback(values, newList)
        DiffUtil.calculateDiff(messageDiffUtilCallback).dispatchUpdatesTo(this)
    }

    class RoomDiffUtilCallback(
        private val mOldList: List<AnimeInfo>,
        private val mNewList: List<AnimeInfo>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = mOldList.size

        override fun getNewListSize(): Int = mNewList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            mOldList[oldItemPosition] == mNewList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            mOldList[oldItemPosition].id == mNewList[newItemPosition].id
    }
}
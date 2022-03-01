package com.example.shikimoriandroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.AnimeItemBinding
import com.example.shikimoriandroid.databinding.FragmentMainAnimeListItemBinding
import com.example.shikimoriandroid.model.AnimeInfo

class AnimeListAdapter(
    private val values: MutableList<AnimeInfo>,
    private val glide: GlideAdapter,
    private val listener: OnAnimeClickListener
):RecyclerView.Adapter<AnimeListAdapter.AnimeItemViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_item, parent, false)
        return AnimeItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeItemViewHolder, position: Int) {
        holder.bind(values[position], listener)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class AnimeItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = AnimeItemBinding.bind(view)

        fun bind(anime: AnimeInfo, listener: OnAnimeClickListener) = with(binding) {
            glide.loadImage("https://shikimori.one/${anime.poster.original}", animeItemPoster)
            animeItemName.text = anime.nameRus
            score.text = anime.score
            view.setOnClickListener {
                listener.onAnimeClick(anime.id)
            }
        }
    }

    fun update(newList: List<AnimeInfo>) {
        val messageDiffUtilCallback =
            RoomDiffUtilCallback(values, newList)
        DiffUtil.calculateDiff(messageDiffUtilCallback).dispatchUpdatesTo(this)
    }

    interface OnAnimeClickListener {
        fun onAnimeClick(id: Int)
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
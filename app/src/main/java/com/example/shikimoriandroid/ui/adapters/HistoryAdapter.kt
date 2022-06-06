package com.example.shikimoriandroid.ui.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.user.History
import com.example.shikimoriandroid.ui.viewholder.HistoryItemViewHolder

class HistoryAdapter(
    private val glide: GlideAdapter,
    private val onHistoryClick: (animeId: Int) -> Unit
) : RecyclerView.Adapter<HistoryItemViewHolder>() {

    var historyList: List<History> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return HistoryItemViewHolder(view, glide)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(historyList[position], onHistoryClick)
    }

    override fun getItemCount(): Int = historyList.size
}
package com.example.shikimoriandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.data.model.anime.Role
import com.example.shikimoriandroid.ui.viewholder.PersonItemViewHolder

class PersonAdapter(
    private val glide: GlideAdapter,
    private val onPersonClick: (personId: Int) -> Unit
) :
    RecyclerView.Adapter<PersonItemViewHolder>() {

    var roles: List<Role> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.person_list_item, parent, false)
        return PersonItemViewHolder(view, glide)
    }

    override fun onBindViewHolder(holder: PersonItemViewHolder, position: Int) {
        holder.bind(roles[position], onPersonClick)
    }

    override fun getItemCount(): Int = roles.size
}
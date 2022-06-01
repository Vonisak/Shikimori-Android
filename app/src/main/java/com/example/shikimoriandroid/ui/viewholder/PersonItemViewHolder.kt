package com.example.shikimoriandroid.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.data.model.anime.Role
import com.example.shikimoriandroid.databinding.PersonListItemBinding
import com.example.shikimoriandroid.ui.adapters.GlideAdapter

class PersonItemViewHolder(val view: View, private val glideAdapter: GlideAdapter) :
    RecyclerView.ViewHolder(view) {
    val binding = PersonListItemBinding.bind(view)

    fun bind(role: Role, onPersonClick: (personId: Int) -> Unit) = with(binding) {
        glideAdapter.loadImage("https://shikimori.one/${role.personPreview?.image?.original}", image)
        name.text = role.personPreview?.nameRus
        roles.text = role.rolesRus.fold("") {str, _role -> "$_role${if (role.rolesRus.size != 1) "\n" else ""}$str" }
        name.setOnClickListener {
            onPersonClick(role.personPreview?.id!!)
        }
        image.setOnClickListener {
            onPersonClick(role.personPreview?.id!!)
        }
    }
}
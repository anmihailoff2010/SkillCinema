package com.example.skillcinema.ui.adapters.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.app.loadImage
import com.example.skillcinema.data.PROFESSIONS
import com.example.skillcinema.databinding.ItemStaffDetailFilmBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes

class ItemFilmPersonViewHolder(
    private val binding: ItemStaffDetailFilmBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: MyAdapterTypes.ItemFilmPerson, clickActor: (actorId: Int) -> Unit) {
        binding.apply {
            actorAvatarFilmDetail.loadImage(item.person.poster)
            actorNameFilmDetail.text = item.person.name
            if (item.person.professionKey == "ACTOR") {
                actorRoleFilmDetail.text = item.person.description
            } else {
                actorRoleFilmDetail.text = PROFESSIONS[item.person.professionKey]
            }
        }
        binding.root.setOnClickListener { clickActor(item.person.personId) }
    }
}
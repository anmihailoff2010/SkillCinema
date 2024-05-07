package com.example.skillcinema.ui.adapters.holders

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.app.loadImage
import com.example.skillcinema.databinding.ItemSimilarBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes


class ItemSimilarViewHolder(private val binding: ItemSimilarBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindNextShow(clickNextButton: () -> Unit) {
        binding.apply {
            showAll.isInvisible = false
            itemFilm.isInvisible = true
        }

        binding.btnArrowShowAll.setOnClickListener { clickNextButton() }
    }

    fun bindItem(item: MyAdapterTypes.ItemFilmSimilar, clickFilms: (filmId: Int) -> Unit) {
        binding.apply {
            showAll.isInvisible = true
            itemFilm.isInvisible = false
            itemFilmName.text = item.similar.name
            itemFilmPoster.loadImage(item.similar.poster)
            itemFilmRating.isVisible = false
        }
        binding.itemFilm.setOnClickListener { clickFilms(item.similar.similarId) }
    }
}
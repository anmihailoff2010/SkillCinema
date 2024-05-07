package com.example.skillcinema.ui.adapters.holders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.app.loadImage
import com.example.skillcinema.databinding.ItemSearchFilmBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes


class ItemSearchFilmViewHolder(val binding: ItemSearchFilmBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindItem(item: MyAdapterTypes.ItemSearchFilms, onClick: (Int) -> Unit) {
        binding.apply {
            itemFilmPoster.loadImage(item.film.posterUrl)
            itemFilmName.text = item.film.nameRu
            itemFilmGenre.text = item.film.genres.joinToString(", ") { it.genre }
            itemFilmRating.isVisible = false
        }
        binding.itemFilmPoster.setOnClickListener { onClick(item.film.filmId) }
    }
}
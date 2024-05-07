package com.example.skillcinema.ui.adapters.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.app.loadImage
import com.example.skillcinema.databinding.ItemGalleryFilmDetailBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes


class ItemImageForFilmDetailViewHolder(
    private val binding: ItemGalleryFilmDetailBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: MyAdapterTypes.ItemFilmImage) {
        binding.galleryImageFilmDetail.loadImage(item.image.preview)
    }
}
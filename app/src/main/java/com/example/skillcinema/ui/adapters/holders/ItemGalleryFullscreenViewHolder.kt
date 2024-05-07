package com.example.skillcinema.ui.adapters.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.app.loadImage
import com.example.skillcinema.databinding.ItemGalleryFullscreenBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes


class ItemGalleryFullscreenViewHolder(val binding: ItemGalleryFullscreenBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bindItem(image: MyAdapterTypes.ItemGalleryFullScreen) {
            binding.galleryImageFullscreen.loadImage(image.image.image)
        }
}
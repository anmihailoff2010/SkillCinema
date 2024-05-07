package com.example.skillcinema.ui.adapters.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.app.loadImage
import com.example.skillcinema.databinding.ItemGalleryImageBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes


class ItemGalleryViewHolder(val binding: ItemGalleryImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bindItem(image: MyAdapterTypes.ItemGalleryImage, onClick: () -> Unit) {
            binding.galleryImage.loadImage(image.image.image)
            binding.root.setOnClickListener { onClick() }
        }
}
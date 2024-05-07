package com.example.skillcinema.ui.adapters.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.app.loadImage
import com.example.skillcinema.databinding.ItemSearchPersonBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes


class ItemSearchPersonViewHolder(private val binding: ItemSearchPersonBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindItem(item: MyAdapterTypes.ItemSearchPersons, onClick: (personId: Int) -> Unit) {
        binding.apply {
            itemPersonPoster.loadImage(item.person.posterUrl)
            itemPersonName.text = item.person.nameRu
        }
        binding.root.setOnClickListener { onClick(item.person.personId) }
    }
}
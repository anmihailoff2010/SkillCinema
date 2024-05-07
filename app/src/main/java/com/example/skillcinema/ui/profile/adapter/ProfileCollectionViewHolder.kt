package com.example.skillcinema.ui.profile.adapter

import android.graphics.drawable.Icon
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.databinding.ItemProfileCollectionBinding

class ProfileCollectionViewHolder(private val binding: ItemProfileCollectionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: ProfileAdapterTypes.ItemCollection) {
        binding.apply {
            itemProfileCollectionIcon.setImageIcon(Icon.createWithResource(binding.root.context, item.collection.icon))
            itemProfileCollectionLabel.text = item.collection.name
            itemProfileCollectionCountBtn.text = item.collection.count.toString()
        }
        binding.root.setOnClickListener {  }
    }
}
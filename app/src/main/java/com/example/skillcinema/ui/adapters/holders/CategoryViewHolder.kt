package com.example.skillcinema.ui.adapters.holders

import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.CategoriesFilms
import com.example.skillcinema.databinding.ItemCategoryListBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes
import com.example.skillcinema.ui.adapters.MyListAdapter
import com.example.skillcinema.ui.home.HomeViewModel

class CategoryViewHolder(
    private val binding: ItemCategoryListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(
        maxListSize: Int,
        item: HomeViewModel.Companion.HomeList,
        clickNextButton: (category: CategoriesFilms) -> Unit,
        clickFilms: (filmId: Int) -> Unit
    ) {
        val adapter =
            MyListAdapter(
                maxListSize,
                { clickNextButton(item.category) },
                { clickFilms(it) })
        adapter.submitList(item.filmList.map { MyAdapterTypes.ItemFilmWithGenre(it) })
        binding.titleCategory.text = item.category.text
        binding.filmList.adapter = adapter
        binding.tvTitleShowAll.apply {
            this.isInvisible = item.filmList.size < maxListSize
            this.setOnClickListener { clickNextButton(item.category) }
        }
    }
}
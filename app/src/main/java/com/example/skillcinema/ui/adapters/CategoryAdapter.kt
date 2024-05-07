package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.CategoriesFilms
import com.example.skillcinema.databinding.ItemCategoryListBinding
import com.example.skillcinema.ui.adapters.holders.CategoryViewHolder
import com.example.skillcinema.ui.home.HomeViewModel

class CategoryAdapter(
    private val maxListSize: Int,
    private val category: List<HomeViewModel.Companion.HomeList>,
    private val clickNextButton: (category: CategoriesFilms) -> Unit,
    private val clickFilms: (filmId: Int) -> Unit
) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
        ItemCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindItem(
            maxListSize,
            category[position],
            { clickNextButton(it) },
            { clickFilms(it) }
        )
    }

    override fun getItemCount() = category.size
}
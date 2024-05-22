package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ItemFilmBinding
import com.example.skillcinema.databinding.ItemSearchFilmBinding
import com.example.skillcinema.databinding.ItemSearchPersonBinding
import com.example.skillcinema.ui.adapters.holders.ItemFilmViewHolder
import com.example.skillcinema.ui.adapters.holders.ItemSearchFilmViewHolder
import com.example.skillcinema.ui.adapters.holders.ItemSearchPersonViewHolder

class MyPagingAdapter(
    private val onClick: (Int) -> Unit
) : PagingDataAdapter<MyAdapterTypes, RecyclerView.ViewHolder>(MyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_film -> {
                ItemFilmViewHolder(ItemFilmBinding.inflate(inflater, parent, false))
            }
            R.layout.item_search_film -> {
                ItemSearchFilmViewHolder(ItemSearchFilmBinding.inflate(inflater, parent, false))
            }
            R.layout.item_search_person -> {
                ItemSearchPersonViewHolder(ItemSearchPersonBinding.inflate(inflater, parent, false))
            }
            else -> throw IllegalArgumentException("Unknown ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemFilmViewHolder -> {
                val item = getItem(position) as? MyAdapterTypes.ItemFilmWithGenre
                item?.let { holder.bindItem(it, onClick) }
            }
            is ItemSearchFilmViewHolder -> {
                val item = getItem(position) as? MyAdapterTypes.ItemSearchFilms
                item?.let { holder.bindItem(it, onClick) }
            }
            is ItemSearchPersonViewHolder -> {
                val item = getItem(position) as? MyAdapterTypes.ItemSearchPersons
                item?.let { holder.bindItem(it, onClick) }
            }
            else -> throw IllegalArgumentException("Unknown ViewHolder type: ${holder.javaClass.simpleName}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyAdapterTypes.ItemFilmWithGenre -> R.layout.item_film
            is MyAdapterTypes.ItemSearchFilms -> R.layout.item_search_film
            is MyAdapterTypes.ItemSearchPersons -> R.layout.item_search_person
            else -> throw IllegalArgumentException("Unknown item type at position $position")
        }
    }
}

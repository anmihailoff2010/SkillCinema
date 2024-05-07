package com.example.skillcinema.ui.profile.adapter

import com.example.skillcinema.db.model.FilmWithGenres

sealed class ProfileAdapterTypes {
    data class ItemDB(val film: FilmWithGenres): ProfileAdapterTypes()
    data class ItemCollection(val collection: CollectionDB): ProfileAdapterTypes()
}

data class CollectionDB(
    val name: String,
    val count: Int,
    val icon: Int
)

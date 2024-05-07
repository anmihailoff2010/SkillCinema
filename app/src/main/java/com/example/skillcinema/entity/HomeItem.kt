package com.example.skillcinema.entity

import com.example.skillcinema.api.old.filmbyfilter.Genre


interface HomeItem {
    val filmId: Int
    val posterUrlPreview: String
    val nameRu: String?
    val rating: String?
    val genres: List<Genre>
}
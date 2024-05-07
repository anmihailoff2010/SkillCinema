package com.example.skillcinema.domain

import com.example.skillcinema.data.CinemaRepository
import javax.inject.Inject

class GetFilmMarkersUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    suspend fun addMarkers(filmId: Int) = repository.addMarkersToFilm(filmId)

    suspend fun updateMarkers(
        filmId: Int,
        isFavorite: Int,
        inCollection: Int,
        isViewed: Int
    ) = repository.updateFilmMarkers(filmId, isFavorite, inCollection, isViewed)

    fun executeMarkersByFilm(filmId: Int) = repository.getFilmMarkers(filmId)
}
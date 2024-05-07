package com.example.skillcinema.domain

import com.example.skillcinema.data.CinemaRepository
import javax.inject.Inject

class GetGalleryByIdUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    suspend fun executeGalleryByFilmId(filmId: Int) =
        repository.getFilmGallery(filmId)
}
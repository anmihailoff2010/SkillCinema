package com.example.skillcinema.domain

import com.example.skillcinema.data.CinemaRepository
import com.example.skillcinema.db.model.FilmWithDetailInfo
import javax.inject.Inject

class GetFilmByIdUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    suspend fun executeFilmDetailInfoById(filmId: Int): FilmWithDetailInfo {
        return repository.getDetailInfoByFilm(filmId)
    }
}
package com.example.skillcinema.domain

import com.example.skillcinema.data.CinemaRepository
import com.example.skillcinema.db.model.FilmPersons
import javax.inject.Inject

class GetActorsListUseCase @Inject constructor(
    private val newRepository: CinemaRepository
) {
    suspend fun executePersonsList(filmId: Int): List<FilmPersons> {
        return newRepository.getPersonsByFilm(filmId)
    }
}
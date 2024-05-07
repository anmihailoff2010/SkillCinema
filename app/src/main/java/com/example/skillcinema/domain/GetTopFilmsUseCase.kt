package com.example.skillcinema.domain

import androidx.paging.PagingData
import com.example.skillcinema.data.CinemaRepository
import com.example.skillcinema.data.ParamsFilterFilm
import com.example.skillcinema.db.model.FilmWithGenres
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopFilmsUseCase @Inject constructor(
    private val repository: CinemaRepository
) {

    suspend fun executeTopFilms(
        topType: String,
        page: Int? = 1,
        filters: ParamsFilterFilm = ParamsFilterFilm(),
    ): List<FilmWithGenres> {
        return repository.getFilmsTopByCategoryList(topType, page, filters)
    }

    fun executeTopFilmsPaging(categoryName: String): Flow<PagingData<FilmWithGenres>> {
        return repository.getFilmsTopByCategoryPaging(categoryName)
    }
}


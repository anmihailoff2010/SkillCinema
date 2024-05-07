package com.example.skillcinema.domain

import androidx.paging.PagingData
import com.example.skillcinema.api.model.FilmByFilter
import com.example.skillcinema.data.CinemaRepository
import com.example.skillcinema.data.ParamsFilterFilm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetFilmListUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    fun executeFilmsByFilter(
        filters: StateFlow<ParamsFilterFilm>
    ): Flow<PagingData<FilmByFilter>> {
        return repository.getFilmsByFilter(filters)
    }
}
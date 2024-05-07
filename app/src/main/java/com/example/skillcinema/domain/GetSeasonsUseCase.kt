package com.example.skillcinema.domain

import com.example.skillcinema.data.CinemaRepository
import javax.inject.Inject

class GetSeasonsUseCase @Inject constructor(private val repository: CinemaRepository) {
    fun executeSeasons(seriesId: Int) = repository.getSeasonsById(seriesId)
}
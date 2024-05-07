package com.example.skillcinema.data

import com.example.skillcinema.api.old.KinopoiskApi
import com.example.skillcinema.api.old.filmbyfilter.ResponseGenresCountries
import javax.inject.Inject

class RepositoryAPI @Inject constructor(
    private val apiService: KinopoiskApi
) {
    suspend fun getGenresCountries(): ResponseGenresCountries = apiService.getGenresCountries()
}
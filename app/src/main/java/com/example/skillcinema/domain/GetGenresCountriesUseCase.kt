package com.example.skillcinema.domain

import com.example.skillcinema.api.old.filmbyfilter.ResponseGenresCountries
import com.example.skillcinema.data.RepositoryAPI
import javax.inject.Inject

class GetGenresCountriesUseCase @Inject constructor(private val repositoryAPI: RepositoryAPI) {
    suspend fun executeGenresCountries(): ResponseGenresCountries {
        return repositoryAPI.getGenresCountries()
    }
}
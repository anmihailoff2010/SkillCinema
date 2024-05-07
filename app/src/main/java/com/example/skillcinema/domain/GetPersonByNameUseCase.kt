package com.example.skillcinema.domain

import androidx.paging.PagingData
import com.example.skillcinema.api.model.ItemPerson
import com.example.skillcinema.data.CinemaRepository
import com.example.skillcinema.data.ParamsFilterFilm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetPersonByNameUseCase @Inject constructor(
    private val repository: CinemaRepository
) {
    fun executePerson(personName: StateFlow<ParamsFilterFilm>): Flow<PagingData<ItemPerson>> {
        return repository.getPersonsByName(personName)
    }
}
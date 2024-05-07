package com.example.skillcinema.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.api.model.FilmByFilter
import com.example.skillcinema.api.model.ItemPerson
import com.example.skillcinema.api.old.filmbyfilter.FilterCountry
import com.example.skillcinema.api.old.filmbyfilter.FilterGenre
import com.example.skillcinema.data.ParamsFilterFilm
import com.example.skillcinema.domain.GetFilmListUseCase
import com.example.skillcinema.domain.GetGenresCountriesUseCase
import com.example.skillcinema.domain.GetPersonByNameUseCase
import com.example.skillcinema.entity.FilterCountryGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ViewModelSearch @Inject constructor(
    private val getGenresCountriesUseCase: GetGenresCountriesUseCase,
    private val getFilmListUseCase: GetFilmListUseCase,
    private val getPersonByNameUseCase: GetPersonByNameUseCase
) : ViewModel() {

    private val _filterFlow = MutableStateFlow(ParamsFilterFilm())
    val filterFlow = _filterFlow.asStateFlow()

    private var countriesList: List<FilterCountry> = emptyList()
    private var genresList: List<FilterGenre> = emptyList()

    private val _filterValuesCountriesGenres = MutableStateFlow<List<FilterCountryGenre>>(emptyList())
    val filterValuesCountriesGenres = _filterValuesCountriesGenres.asStateFlow()

    val newFilms: Flow<PagingData<FilmByFilter>> = getFilmListUseCase
        .executeFilmsByFilter(filters = _filterFlow).cachedIn(viewModelScope)

    val persons: Flow<PagingData<ItemPerson>> = getPersonByNameUseCase
        .executePerson(personName = _filterFlow).cachedIn(viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getGenresCountriesUseCase.executeGenresCountries()
            countriesList = response.countries.sortedBy { it.name }.filter { it.name.isNotEmpty() }
            genresList = response.genres.sortedBy { it.name }.filter { it.name.isNotEmpty() }
            _filterValuesCountriesGenres.value = countriesList
        }
    }

    fun updateFiltersFull(filterFilm: ParamsFilterFilm) {
        if (_filterFlow.value != filterFilm) {
            _filterFlow.value = filterFilm
        }
    }

    fun updateFilterCountriesGenres(type: String, keyword: String) {
        _filterValuesCountriesGenres.value = when (type) {
            KEY_COUNTRY -> countriesList.filter { it.name.startsWith(keyword, ignoreCase = true) }
            KEY_GENRE -> genresList.filter { it.name.startsWith(keyword, ignoreCase = true) }
            else -> emptyList()
        }
    }

    fun setFilterValues(filterType: String) {
        _filterValuesCountriesGenres.value = when (filterType) {
            KEY_COUNTRY -> countriesList
            KEY_GENRE -> genresList
            else -> emptyList()
        }
    }

    fun getFiltersFull(): ParamsFilterFilm {
        return _filterFlow.value
    }

    companion object {
        private const val KEY_COUNTRY = "country"
        private const val KEY_GENRE = "genre"
    }
}

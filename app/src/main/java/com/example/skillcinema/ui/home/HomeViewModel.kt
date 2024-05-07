package com.example.skillcinema.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.app.prepareToShow
import com.example.skillcinema.data.CategoriesFilms
import com.example.skillcinema.data.ParamsFilterFilm
import com.example.skillcinema.data.TOP_TYPES
import com.example.skillcinema.db.model.FilmWithGenres
import com.example.skillcinema.domain.GetTopFilmsUseCase
import com.example.skillcinema.ui.StateLoading
import com.example.skillcinema.ui.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopFilmsUseCase: GetTopFilmsUseCase
) : ViewModel() {

    init {
        getFilmsByCategories()
    }

    private val _homePageList = MutableStateFlow<List<HomeList>>(emptyList())
    val homePageList = _homePageList.asStateFlow()

    private val _loadCategoryState = MutableStateFlow<StateLoading>(StateLoading.Default)
    val loadCategoryState = _loadCategoryState.asStateFlow()

    fun getFilmsByCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadCategoryState.value = StateLoading.Loading
                val list = listOf(
                    HomeList(
                        category = CategoriesFilms.BEST,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = TOP_TYPES.getValue(CategoriesFilms.BEST),
                            page = 1
                        ).prepareToShow(20)
                    ),
                    HomeList(
                        category = CategoriesFilms.PREMIERS,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = CategoriesFilms.PREMIERS.name,
                            page = null
                        ).prepareToShow(20)
                    ),
                    HomeList(
                        category = CategoriesFilms.AWAIT,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = TOP_TYPES.getValue(CategoriesFilms.AWAIT),
                            page = 1
                        ).prepareToShow(20)
                    ),
                    HomeList(
                        category = CategoriesFilms.POPULAR,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = TOP_TYPES.getValue(CategoriesFilms.POPULAR),
                            page = 1
                        ).prepareToShow(20)
                    ),
                    HomeList(
                        category = CategoriesFilms.TV_SERIES,
                        filmList = getTopFilmsUseCase.executeTopFilms(
                            topType = TOP_TYPES.getValue(CategoriesFilms.TV_SERIES),
                            filters = ParamsFilterFilm(
                                type = TOP_TYPES.getValue(CategoriesFilms.TV_SERIES),
                                ratingFrom = 6
                            ),
                            page = 1
                        ).prepareToShow(20)
                    )
                )
                _homePageList.value = list
                _loadCategoryState.value = StateLoading.Success
            } catch (e: Throwable) {
                Log.e(TAG, "Error loading categories: ${e.message}", e) // Добавляем лог ошибки
                _loadCategoryState.value = StateLoading.Error(e.message.toString())
            }
        }
    }

    companion object {
        data class HomeList(
            val category: CategoriesFilms,
            val filmList: List<FilmWithGenres>
        )
    }
}


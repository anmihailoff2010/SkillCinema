package com.example.skillcinema.ui.allfilmsbycategory

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.skillcinema.data.CategoriesFilms
import com.example.skillcinema.data.TOP_TYPES
import com.example.skillcinema.db.model.FilmWithGenres
import com.example.skillcinema.domain.GetTopFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelAllFilms @Inject constructor(
    private val getTopFilmsUseCase: GetTopFilmsUseCase
) : ViewModel() {

    lateinit var allFilmsByCategory: Flow<PagingData<FilmWithGenres>>

    fun setCurrentCategory(category: CategoriesFilms) {
        val categoryForRequest =
            if (category.name == CategoriesFilms.PREMIERS.name) category.name
            else TOP_TYPES.getValue(category)

        allFilmsByCategory = getTopFilmsUseCase
            .executeTopFilmsPaging(categoryName = categoryForRequest)
    }
}
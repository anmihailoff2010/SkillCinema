package com.example.skillcinema.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.skillcinema.api.model.*
import com.example.skillcinema.app.converterInMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import com.example.skillcinema.api.CinemaApi
import com.example.skillcinema.data.*
import com.example.skillcinema.db.CinemaDao
import com.example.skillcinema.db.model.*
import java.io.IOException
import java.util.Calendar
import javax.inject.Inject

class CinemaRepository @Inject constructor(
    private val apiService: CinemaApi,
    private val apiDatabase: CinemaDao,
) {
    private val calendar = Calendar.getInstance()

    init {
        kotlinx.coroutines.runBlocking(Dispatchers.IO) {
            apiDatabase.insertCollection(CollectionFilms(COLLECTION_FAVORITE_NAME))
            apiDatabase.insertCollection(CollectionFilms(COLLECTION_BOOKMARK_NAME))
        }
    }

    suspend fun getFilmsTopByCategoryList(
        categoryName: String,
        page: Int? = 1,
        filters: ParamsFilterFilm = ParamsFilterFilm(),
        year: Int = calendar.get(Calendar.YEAR),
        month: String = (calendar.get(Calendar.MONTH) + 1).converterInMonth()
    ): List<FilmWithGenres> {
        return try {
            apiDatabase.clearFilmTopTypesByCategory(categoryName)
            var genresForDb: List<List<NewFilmGenres>> = emptyList()
            val filmsForDb: List<FilmsShortInfo> = when (categoryName) {
                CategoriesFilms.PREMIERS.name -> {
                    val response: List<FilmPremier> =
                        apiService.getPremier(year = year, month = month).films
                    if (response.isNotEmpty()) {
                        genresForDb = response.map { it.convertForDbGenres() }
                        response.map { it.convertForDbShortInfo() }
                    } else emptyList()
                }

                CategoriesFilms.TV_SERIES.name -> {
                    val response: List<FilmByFilter> = apiService.getFilmsByFilter(
                        countries = filters.countries.keys.joinToString(","),
                        genres = filters.genres.keys.joinToString(","),
                        order = filters.order,
                        type = categoryName,
                        ratingFrom = filters.ratingFrom,
                        ratingTo = filters.ratingTo,
                        yearFrom = filters.yearFrom,
                        yearTo = filters.yearTo,
                        imdbId = filters.imdbId,
                        keyword = filters.keyword,
                        page = page!!
                    ).films
                    if (response.isNotEmpty()) {
                        genresForDb = response.map { it.convertForDbGenres() }
                        response.map { it.convertForDbShortInfo() }
                    } else emptyList()
                }

                else -> {
                    val response: List<FilmTop> =
                        apiService.getFilmsTop(type = categoryName, page = page!!).films
                    if (response.isNotEmpty()) {
                        genresForDb = response.map { it.convertForDbGenres() }
                        response.map { it.convertForDbShortInfo() }
                    } else emptyList()
                }
            }
            filmsForDb.forEach { apiDatabase.deleteFilmGenres(it.filmId) }
            genresForDb.forEach { apiDatabase.insertFilmGenres(it) }

            apiDatabase.insertFilmTopTypes(filmsForDb.map {
                NewFilmTopType(filmId = it.filmId, categoryName = categoryName)
            })
            apiDatabase.insertFilmShortInfo(filmsForDb)
            apiDatabase.getFilmsByTopType(categoryName)
        } catch (e: HttpException) {
            apiDatabase.getFilmsByTopType(categoryName)
        } catch (e: IOException) {
            throw IOException(e)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getFilmsTopByCategoryPaging(
        categoryName: String,
    ): Flow<PagingData<FilmWithGenres>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = MediatorFilmTop(
                apiService = apiService,
                apiDatabase = apiDatabase,
                categoryName = categoryName,
                year = calendar.get(Calendar.YEAR),
                month = (calendar.get(Calendar.MONTH) + 1).converterInMonth()
            ),
            pagingSourceFactory = { apiDatabase.getFilmByTopCategoryPaging(categoryName = categoryName) }
        ).flow
    }

    fun getFilmsByFilter(filters: StateFlow<ParamsFilterFilm>): Flow<PagingData<FilmByFilter>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { SearchFilmsPagingSource(apiService, filters) }
        ).flow
    }

    fun getPersonsByName(personName: StateFlow<ParamsFilterFilm>): Flow<PagingData<ItemPerson>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { SearchPersonsPagingSource(apiService, personName) }
        ).flow
    }

    suspend fun getDetailInfoByFilm(filmId: Int): FilmWithDetailInfo {
        return try {
            val responseDetail: ResponseFilmDetailById = apiService.getFilmById(filmId)

            apiDatabase.insertOneFilmShortInfo(
                FilmsShortInfo(
                    filmId = responseDetail.kinopoiskId,
                    name = responseDetail.nameRu ?: responseDetail.nameEn
                    ?: responseDetail.nameOriginal ?: "",
                    poster = responseDetail.posterUrl,
                    rating = responseDetail.ratingKinopoisk.toString()
                )
            )

            apiDatabase.clearCountriesByFilm(filmId)
            apiDatabase.clearGalleryByFilm(filmId)
            apiDatabase.cleaFilmPersons(filmId)
            apiDatabase.clearSimilar(filmId)

            apiDatabase.insertFilmDetailInfo(responseDetail.convertToDbDetailInfo())

            val responseFilmPersons: List<ResponsePersonsByFilmId> = apiService.getPersons(filmId)
            apiDatabase.insertFilmPersons(responseFilmPersons.map { it.convertToDbFilmPersons(filmId) })

            if (responseDetail.serial == true) {
                apiDatabase.clearSeriesEpisodes(filmId)

                val responseSeasons: ResponseSeasons = apiService.getSeasons(filmId)

                responseSeasons.seasons.forEach { season ->
                    apiDatabase.insertSeasonEpisodes(season.episodes.map { episode ->
                        NewSeasonEpisode(
                            filmId = filmId,
                            seriesNumber = episode.seasonNumber,
                            episodeNumber = episode.episodeNumber,
                            name = episode.nameRu ?: episode.nameEn,
                            date = episode.releaseDate,
                            synopsis = episode.synopsis
                        )
                    })
                }
            }

            val countriesToDb = responseDetail.countries.map { country ->
                NewFilmCountries(
                    filmId = filmId,
                    country = country.country
                )
            }
            apiDatabase.insertFilmCountries(countriesToDb)

            GALLERY_TYPES.keys.forEach { galleryType ->
                val images: ResponseGalleryByFilmId =
                    apiService.getFilmImages(filmId, galleryType, 1)
                val gallery = images.convertToDbGallery(filmId, galleryType)
                apiDatabase.insertFilmGallery(gallery)
            }

            val responseSimilarFilms: ResponseSimilarFilmsByFilmId =
                apiService.getSimilarFilms(filmId)
            val similarToDb: List<NewFilmSimilar>? = responseSimilarFilms.convertToDbSimilar(filmId)
            if (similarToDb != null) {
                apiDatabase.insertSimilar(similarToDb)
            }

            apiDatabase.getCurrentFilmDetailInfo(filmId)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getPersonsByFilm(personId: Int): List<FilmPersons> {
        return apiDatabase.getAllPersonsByFilm(personId)
    }

    suspend fun getPersonDetailInfo(personId: Int): PersonWithDetailInfo {
        apiDatabase.clearPersonFilms(personId)

        val response: ResponsePersonById = apiService.getPersonById(personId)

        val tempFilms = response.films
        if (tempFilms != null) {
            apiDatabase.insertPersonFilms(tempFilms.map {
                NewPersonFilms(
                    personId = personId,
                    filmId = it.filmId,
                    professionKey = it.professionKey
                )
            })
        }
        apiDatabase.insertOnePersonShortInfo(response.convertToDb())
        return apiDatabase.getPersonWithFilms(personId)
    }

    suspend fun getFilmShortInfo(filmId: Int): FilmsShortInfo {
        val count = apiDatabase.getFilmShortInfoCount(filmId)
        if (count != 0) {
            apiDatabase.getFilmShortInfo(filmId)
        } else {
            val response = apiService.getFilmById(filmId)
            apiDatabase.insertOneFilmShortInfo(response.convertToShortInfo())
        }
        return apiDatabase.getFilmShortInfo(filmId)
    }

    suspend fun getFilmsByPerson(personId: Int): List<PersonFilms> {
        return apiDatabase.getFilmsByPerson(personId)
    }

    suspend fun getFilmGallery(filmId: Int): List<FilmImage> {
        return apiDatabase.getFilmGallery(filmId)
    }

    fun getSeasonsById(seriesId: Int): List<SeasonEpisode> {
        return apiDatabase.getSeriesSeasonsWithEpisodes(seriesId)
    }


    // начал писать, но не реализовал до конца
    // работа с маркерами по фильму
    suspend fun addMarkersToFilm(filmId: Int) {
        apiDatabase.insertFilmMarkers(FilmMarkers(filmId, 0, 0, 0))
    }

    fun getFilmMarkers(filmId: Int): Flow<FilmMarkers> {
        return apiDatabase.getFilmMarkers(filmId)
    }

    fun getAllViewedFilms(): Flow<List<FilmWithGenres>> {
        return apiDatabase.getAllViewedFilms()
    }

    suspend fun updateFilmMarkers(
        filmId: Int,
        isFavorite: Int,
        inCollection: Int,
        isViewed: Int
    ) {
        apiDatabase.updateFilmMarkers(filmId, isFavorite, inCollection, isViewed)
        if (isFavorite == 1) insertFilmInCollection(filmId, COLLECTION_FAVORITE_NAME)
        else {
            apiDatabase.deleteFilmFromCollection(filmId, COLLECTION_FAVORITE_NAME)
            val size = apiDatabase.getCollectionSize(COLLECTION_FAVORITE_NAME)
            apiDatabase.updateCollectionSize(COLLECTION_FAVORITE_NAME, size)
        }
        if (inCollection == 1) insertFilmInCollection(filmId, COLLECTION_BOOKMARK_NAME)
        else {
            apiDatabase.deleteFilmFromCollection(filmId, COLLECTION_BOOKMARK_NAME)
            val size = apiDatabase.getCollectionSize(COLLECTION_BOOKMARK_NAME)
            apiDatabase.updateCollectionSize(COLLECTION_BOOKMARK_NAME, size)
        }
    }

    fun getCountFavoriteFilms(): Int {
        return apiDatabase.getCountFavoriteFilms()
    }

    suspend fun insertFilmToHistory(filmId: Int) {
        apiDatabase.insertHistoryFilms(HistoryFilms(filmId))
    }

    fun getAllFilmsHistory(): Flow<List<FilmWithGenres>> {
        return apiDatabase.getAllFilmsHistory()
    }

    suspend fun clearAllHistoryFilms() {
        apiDatabase.clearHistoryFilms()
    }

    suspend fun clearAllViewedFilms() {
        apiDatabase.clearAllFromViewed()
    }

    private suspend fun insertFilmInCollection(filmId: Int, collectionName: String) {
        val isFilmInCollection = apiDatabase.checkFilmInCollection(collectionName, filmId)
        if (isFilmInCollection == 0) {
            apiDatabase.insertFilmInCollection(
                NewFilmInCollection(
                    collectionName = collectionName,
                    filmId = filmId
                )
            )
            val size = apiDatabase.getCollectionSize(collectionName)
            apiDatabase.updateCollectionSize(collectionName, size)
        }
    }

    suspend fun addCollection(name: String) {
        apiDatabase.insertCollection(CollectionFilms(name))
    }

    fun getAllCollections(): Flow<List<CollectionFilms>> {
        return apiDatabase.getAllCollections()
    }

    fun getFilmsByCollection(collectionName: String): List<FilmWithGenres> {
        return apiDatabase.getAllFilmInCollections(collectionName)
    }

    companion object {
        private const val COLLECTION_FAVORITE_NAME = "Любимые"
        private const val COLLECTION_BOOKMARK_NAME = "Хочу посмотреть"
    }
}
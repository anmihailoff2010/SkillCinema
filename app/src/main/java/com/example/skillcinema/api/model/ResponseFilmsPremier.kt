package com.example.skillcinema.api.model

import com.example.skillcinema.db.model.FilmsShortInfo
import com.example.skillcinema.db.model.NewFilmGenres
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponsePremier(
    @Json(name = "items") val films: List<FilmPremier>,
    @Json(name = "total") val total: Int
)

@JsonClass(generateAdapter = true)
data class FilmPremier(
    @Json(name = "kinopoiskId") val filmId: Int,
    @Json(name = "nameRu") val nameRu: String,
    @Json(name = "nameEn") val nameEn: String,
    @Json(name = "posterUrlPreview") val posterUrlPreview: String,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "genres") val genres: List<Genre>,
//    @Json(name = "countries") val countries: List<Country>,     // не нужно
//    @Json(name = "duration") val duration: Int?,                // не нужно
//    @Json(name = "premiereRu") val premiereRu: String,          // не нужно
//    @Json(name = "year") val year: Int                          // не нужно
)

fun FilmPremier.convertForDbShortInfo(): FilmsShortInfo {
    return FilmsShortInfo(
        filmId = this.filmId,
        name = if (this.nameRu.isNotEmpty()) this.nameRu else if (this.nameEn.isNotEmpty()) this.nameEn else "",
        poster = this.posterUrlPreview,
        rating = null
    )
}
fun FilmPremier.convertForDbGenres(): List<NewFilmGenres> {
    return this.genres.map { genre ->
        NewFilmGenres(
            filmId = this.filmId,
            genre = genre.genre
        )
    }
}
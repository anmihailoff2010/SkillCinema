package com.example.skillcinema.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skillcinema.db.model.CollectionFilms
import com.example.skillcinema.db.model.FilmCountries
import com.example.skillcinema.db.model.FilmDetailInfo
import com.example.skillcinema.db.model.FilmGenres
import com.example.skillcinema.db.model.FilmImage
import com.example.skillcinema.db.model.FilmInCollection
import com.example.skillcinema.db.model.FilmMarkers
import com.example.skillcinema.db.model.FilmPersons
import com.example.skillcinema.db.model.FilmSimilar
import com.example.skillcinema.db.model.FilmTopType
import com.example.skillcinema.db.model.FilmsShortInfo
import com.example.skillcinema.db.model.HistoryFilms
import com.example.skillcinema.db.model.PersonFilms
import com.example.skillcinema.db.model.PersonShortInfo
import com.example.skillcinema.db.model.SeasonEpisode

@Database(
    entities = [
        FilmsShortInfo::class,
        FilmDetailInfo::class,
        FilmPersons::class,
        FilmImage::class,
        FilmGenres::class,
        FilmCountries::class,
        PersonShortInfo::class,
        PersonFilms::class,
        CollectionFilms::class,
        FilmInCollection::class,
        FilmSimilar::class,
        SeasonEpisode::class,
        FilmMarkers::class,
        HistoryFilms::class,
        FilmTopType::class
    ],
    version = 1
)
abstract class CinemaDatabase : RoomDatabase() {
    abstract fun cinemaDao(): CinemaDao
}
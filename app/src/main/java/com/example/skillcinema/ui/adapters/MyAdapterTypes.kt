package com.example.skillcinema.ui.adapters

import com.example.skillcinema.api.model.FilmByFilter
import com.example.skillcinema.api.model.ItemPerson
import com.example.skillcinema.db.model.FilmImage
import com.example.skillcinema.db.model.FilmPersons
import com.example.skillcinema.db.model.FilmSimilar
import com.example.skillcinema.db.model.FilmWithGenres
import com.example.skillcinema.db.model.FilmsShortInfo
import com.example.skillcinema.db.model.SeasonEpisode


sealed class MyAdapterTypes {
    data class ItemFilmWithGenre(val filmWithGenre: FilmWithGenres): MyAdapterTypes()
    data class ItemFilmShortInfo(val filmShortInfo: FilmsShortInfo): MyAdapterTypes()
    data class ItemFilmSimilar(val similar: FilmSimilar): MyAdapterTypes()
    data class ItemFilmPerson(val person: FilmPersons): MyAdapterTypes()
    data class ItemFilmImage(val image: FilmImage): MyAdapterTypes()
    data class ItemEpisode(val season: SeasonEpisode): MyAdapterTypes()
    data class ItemGalleryImage(val image: FilmImage): MyAdapterTypes()
    data class ItemGalleryFullScreen(val image: FilmImage): MyAdapterTypes()
    data class ItemSearchPersons(val person: ItemPerson): MyAdapterTypes()
    data class ItemSearchFilms(val film: FilmByFilter): MyAdapterTypes()
}
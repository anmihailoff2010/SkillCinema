package com.example.skillcinema.di

import android.content.Context
import androidx.room.Room
import com.example.skillcinema.db.CinemaDao
import com.example.skillcinema.db.CinemaDatabase
import com.example.skillcinema.db.old.AppDataBase
import com.example.skillcinema.db.old.FilmDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            "Films"
        ).build()
    }

    @Provides
    fun provideDao(appDataBase: AppDataBase): FilmDao {
        return appDataBase.filmDao()
    }

    @Provides
    fun provideCinemaDatabase(@ApplicationContext context: Context): CinemaDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CinemaDatabase::class.java,
            "Cinema.db"
        ).build()
    }

    @Provides
    fun provideCinemaDao(database: CinemaDatabase): CinemaDao {
        return database.cinemaDao()
    }
}
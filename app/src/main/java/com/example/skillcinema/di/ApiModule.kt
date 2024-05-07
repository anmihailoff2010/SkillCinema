package com.example.skillcinema.di

import com.example.skillcinema.api.CinemaApi
import com.example.skillcinema.api.old.KinopoiskApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Provides
    fun provideAPI(): KinopoiskApi {
        return KinopoiskApi.getInstance()
    }

    @Provides
    fun provideCinemaAPI(): CinemaApi {
        return CinemaApi.getInstance()
    }
}
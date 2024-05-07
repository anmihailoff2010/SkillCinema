package com.example.skillcinema.api.old

import com.example.skillcinema.api.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import com.example.skillcinema.api.old.filmbyfilter.ResponseGenresCountries

interface KinopoiskApi {
    // FragmentHome (TV_SERIES) & FragmentSearch
    @GET("v2.2/films/filters")
    suspend fun getGenresCountries(): ResponseGenresCountries

    companion object {
        private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/"
        private const val API_KEY = "3923fc5d-a906-4cb5-8cbe-f40d9cc6e259"

        private val interceptor = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(API_KEY))
            .build()

        fun getInstance(): KinopoiskApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(interceptor)
                .build()
                .create(KinopoiskApi::class.java)
        }
    }
}
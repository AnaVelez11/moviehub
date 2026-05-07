package com.example.moviehubapp.data.remote

import com.example.moviehubapp.data.remote.dto.GenreListResponseDto
import com.example.moviehubapp.data.remote.dto.MovieDetailDto
import com.example.moviehubapp.data.remote.dto.MovieListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Endpoint 1: Películas populares con paginación
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): MovieListResponseDto

    // Endpoint 2: Buscar películas por título
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): MovieListResponseDto

    // Endpoint 3: Filtrar por género (discover)
    @GET("discover/movie")
    suspend fun discoverMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES",
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): MovieListResponseDto

    // Endpoint 4: Detalle completo de una película
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "es-ES"
    ): MovieDetailDto

    // Endpoint 5: Lista de géneros disponibles
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String = "es-ES"
    ): GenreListResponseDto
}
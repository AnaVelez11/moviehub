package com.example.moviehubapp.domain.repository


import com.example.moviehubapp.domain.model.Genre
import com.example.moviehubapp.domain.model.Movie
import com.example.moviehubapp.domain.model.MovieDetail
import com.example.moviehubapp.domain.model.Resource

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Resource<List<Movie>>
    suspend fun searchMovies(query: String, page: Int): Resource<List<Movie>>
    suspend fun discoverMoviesByGenre(genreId: Int, page: Int): Resource<List<Movie>>
    suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail>
    suspend fun getGenres(): Resource<List<Genre>>
}
package com.example.moviehubapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviehubapp.data.local.entity.GenreEntity
import com.example.moviehubapp.data.local.entity.MovieDetailEntity
import com.example.moviehubapp.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    // Obtener todas las películas para modo offline
    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    suspend fun getAllMovies(): List<MovieEntity>

    // Paginación manual desde la base de datos local
    @Query("SELECT * FROM movies ORDER BY popularity DESC LIMIT :limit OFFSET :offset")
    suspend fun getMoviesPaged(limit: Int, offset: Int): List<MovieEntity>

    // Buscar por título en local (modo offline)
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' ORDER BY popularity DESC")
    suspend fun searchMoviesLocal(query: String): List<MovieEntity>

    // Filtrar por género en local (busca el id dentro del string de genreIds)
    @Query("SELECT * FROM movies WHERE genreIds LIKE '%' || :genreId || '%' ORDER BY popularity DESC")
    suspend fun getMoviesByGenreLocal(genreId: String): List<MovieEntity>

    @Query("DELETE FROM movies")
    suspend fun clearMovies()
}

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Query("SELECT * FROM genres ORDER BY name ASC")
    suspend fun getAllGenres(): List<GenreEntity>

    @Query("DELETE FROM genres")
    suspend fun clearGenres()
}

@Dao
interface MovieDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: MovieDetailEntity)

    @Query("SELECT * FROM movie_details WHERE id = :movieId")
    suspend fun getMovieDetail(movieId: Int): MovieDetailEntity?

    @Query("DELETE FROM movie_details WHERE id = :movieId")
    suspend fun deleteMovieDetail(movieId: Int)
}
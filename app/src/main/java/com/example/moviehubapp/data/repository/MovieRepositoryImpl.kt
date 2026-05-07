package com.example.moviehubapp.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.moviehubapp.data.local.dao.GenreDao
import com.example.moviehubapp.data.local.dao.MovieDao
import com.example.moviehubapp.data.local.dao.MovieDetailDao
import com.example.moviehubapp.data.remote.ApiService
import com.example.moviehubapp.domain.model.Genre
import com.example.moviehubapp.domain.model.Movie
import com.example.moviehubapp.domain.model.MovieDetail
import com.example.moviehubapp.domain.model.Resource
import com.example.moviehubapp.domain.repository.MovieRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val movieDao: MovieDao,
    private val genreDao: GenreDao,
    private val movieDetailDao: MovieDetailDao,
    @ApplicationContext private val context: Context
) : MovieRepository {

    // ── Utilidad: verificar conexión ──────────────────────────────────────────

    fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // ── Películas populares ───────────────────────────────────────────────────

    override suspend fun getPopularMovies(page: Int): Resource<List<Movie>> {
        return if (isOnline()) {
            try {
                val response = apiService.getPopularMovies(page)
                val movies = response.results
                // Persistir en Room solo la primera página para no saturar la BD
                if (page == 1) {
                    movieDao.clearMovies()
                    movieDao.insertMovies(movies.map { it.toEntity() })
                } else {
                    movieDao.insertMovies(movies.map { it.toEntity() })
                }
                Resource.Success(movies.map { it.toDomain() })
            } catch (e: Exception) {
                // Fallback a local si la petición falla
                val local = movieDao.getMoviesPaged(limit = 20, offset = (page - 1) * 20)
                if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
                else Resource.Error("Error de red: ${e.localizedMessage}")
            }
        } else {
            val local = movieDao.getMoviesPaged(limit = 20, offset = (page - 1) * 20)
            if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
            else Resource.Error("Sin conexión y sin datos almacenados")
        }
    }

    // ── Búsqueda por título ───────────────────────────────────────────────────

    override suspend fun searchMovies(query: String, page: Int): Resource<List<Movie>> {
        return if (isOnline()) {
            try {
                val response = apiService.searchMovies(query, page)
                Resource.Success(response.results.map { it.toDomain() })
            } catch (e: Exception) {
                val local = movieDao.searchMoviesLocal(query)
                if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
                else Resource.Error("Error al buscar: ${e.localizedMessage}")
            }
        } else {
            val local = movieDao.searchMoviesLocal(query)
            if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
            else Resource.Error("Sin conexión. No hay resultados locales para \"$query\"")
        }
    }

    // ── Filtrar por género ────────────────────────────────────────────────────

    override suspend fun discoverMoviesByGenre(genreId: Int, page: Int): Resource<List<Movie>> {
        return if (isOnline()) {
            try {
                val response = apiService.discoverMoviesByGenre(genreId, page)
                Resource.Success(response.results.map { it.toDomain() })
            } catch (e: Exception) {
                val local = movieDao.getMoviesByGenreLocal(genreId.toString())
                if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
                else Resource.Error("Error al filtrar: ${e.localizedMessage}")
            }
        } else {
            val local = movieDao.getMoviesByGenreLocal(genreId.toString())
            if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
            else Resource.Error("Sin conexión. No hay películas locales para este género")
        }
    }

    // ── Detalle de película ───────────────────────────────────────────────────

    override suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail> {
        return if (isOnline()) {
            try {
                val dto = apiService.getMovieDetail(movieId)
                movieDetailDao.insertMovieDetail(dto.toEntity())
                Resource.Success(dto.toDomain())
            } catch (e: Exception) {
                val local = movieDetailDao.getMovieDetail(movieId)
                if (local != null) Resource.Success(local.toDomain())
                else Resource.Error("Error al cargar detalle: ${e.localizedMessage}")
            }
        } else {
            val local = movieDetailDao.getMovieDetail(movieId)
            if (local != null) Resource.Success(local.toDomain())
            else Resource.Error("Sin conexión y detalle no almacenado")
        }
    }

    // ── Géneros ───────────────────────────────────────────────────────────────

    override suspend fun getGenres(): Resource<List<Genre>> {
        return if (isOnline()) {
            try {
                val response = apiService.getGenres()
                genreDao.clearGenres()
                genreDao.insertGenres(response.genres.map { it.toEntity() })
                Resource.Success(response.genres.map { it.toDomain() })
            } catch (e: Exception) {
                val local = genreDao.getAllGenres()
                if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
                else Resource.Error("Error al cargar géneros: ${e.localizedMessage}")
            }
        } else {
            val local = genreDao.getAllGenres()
            if (local.isNotEmpty()) Resource.Success(local.map { it.toDomain() })
            else Resource.Error("Sin conexión y géneros no almacenados")
        }
    }
}